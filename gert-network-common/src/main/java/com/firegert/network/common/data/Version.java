package com.firegert.network.common.data;

import com.firegert.network.common.exception.IllegalVersionException;
import lombok.AccessLevel;
import lombok.Builder;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Builder(access = AccessLevel.PUBLIC, builderClassName = "VersionBuilder")
public record Version(String prefix, int major, short minor, short patch, String postfix) {

    private static final String DASH = "-";
    private static final String DOT = ".";
    private static final String REGEX = String.format("^([A-Za-z]+)%s([0-9]+)%s([0-9]+)%s([0-9]+)%s([A-Za-z]+)$",
            DASH, DOT, DOT, DASH);
    private static final Pattern REGEX_PATTERN = Pattern.compile(REGEX);
    private static final String TEXT_PATTERN = "%s" + DASH + "%d" + DOT + "%d" + DOT + "%d" + DASH + "%s";
    private static final int GROUP_NR_PREFIX = 1;
    private static final int GROUP_NR_MAJOR = 2;
    private static final int GROUP_NR_MINOR = 3;
    private static final int GROUP_NR_PATCH = 4;
    private static final int GROUP_NR_POSTFIX = 5;
    private static final String DEFAULT_PREFIX = "V";
    private static final String DEFAULT_POSTFIX = "SNAPSHOT";
    private static final short DEFAULT_ZERO = 0;
    private static final short MIN_PATCH = 1;
    private static final short DEFAULT_PATCH = MIN_PATCH;
    private static final short MAX_PATCH_OR_MINOR = Short.MAX_VALUE - 1;
    private static final int MAX_MAJOR = Integer.MAX_VALUE - 1;
    private static final short MIN_NUMBER = DEFAULT_ZERO;

    public static final Version DEFAULT_VERSION = new Version();

    public static Version fromString(String versionText) {
        if (null == versionText || versionText.trim().isEmpty()) {
            return null;
        }
        Matcher matcher = REGEX_PATTERN.matcher(versionText);
        if (!matcher.matches()) {
            throw new IllegalVersionException(versionText + " is not supported with pattern: " + REGEX);
        }
        return builder()
                .prefix(matcher.group(GROUP_NR_PREFIX))
                .major(Short.parseShort(matcher.group(GROUP_NR_MAJOR)))
                .minor(Short.parseShort(matcher.group(GROUP_NR_MINOR)))
                .patch(Short.parseShort(matcher.group(GROUP_NR_PATCH)))
                .postfix(matcher.group(GROUP_NR_POSTFIX))
                .build();
    }

    public Version(String prefix, int major, short minor, short patch, String postfix) {
        if (null == prefix || prefix.trim().isEmpty()) {
            prefix = DEFAULT_PREFIX;
        }
        if (null == postfix || postfix.trim().isEmpty()) {
            postfix = DEFAULT_POSTFIX;
        }
        this.prefix = prefix;
        this.postfix = postfix;
        if (major < MIN_NUMBER || major > MAX_MAJOR) {
            throw new IllegalVersionException("major is set to: " + major);
        }
        if (minor < MIN_NUMBER || minor > MAX_PATCH_OR_MINOR) {
            throw new IllegalVersionException("minor is set to: " + major);
        }
        if (patch < MIN_NUMBER || patch > MAX_PATCH_OR_MINOR) {
            throw new IllegalVersionException("patch is set to: " + major);
        }
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    public Version(int major, short minor, short patch) {
        this(DEFAULT_PREFIX, major, minor, patch, DEFAULT_POSTFIX);
    }

    public Version(int major, short minor) {
        this(DEFAULT_PREFIX, major, minor, DEFAULT_PATCH, DEFAULT_POSTFIX);
    }

    public Version(int major) {
        this(DEFAULT_PREFIX, major, DEFAULT_ZERO, DEFAULT_PATCH, DEFAULT_POSTFIX);
    }

    public Version() {
        this(DEFAULT_PREFIX, DEFAULT_ZERO, DEFAULT_ZERO, DEFAULT_PATCH, DEFAULT_POSTFIX);
    }

    @Override
    public String toString() {
        return String.format(TEXT_PATTERN, prefix, major, minor, patch, postfix);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Version version = (Version) o;
        return major == version.major && minor == version.minor && patch == version.patch
                && Objects.equals(prefix.toLowerCase(), version.prefix.toLowerCase())
                && Objects.equals(postfix.toLowerCase(), version.postfix.toLowerCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(prefix.toLowerCase(), major, minor, patch, postfix.toLowerCase());
    }

    public Version nextPatch() {
        if (patch + 1 > MAX_PATCH_OR_MINOR) {
            throw new IllegalVersionException("patch cannot be increased more than: " + MAX_PATCH_OR_MINOR);
        }
        return builder().prefix(prefix).major(major).minor(minor).patch(nextShort(patch)).postfix(postfix).build();
    }

    public Version previousPatch() {
        if (patch - 1 < MIN_PATCH) {
            throw new IllegalVersionException("patch cannot be decreased less than: " + MIN_NUMBER);
        }
        return builder().prefix(prefix).major(major).minor(minor).patch(previousShort(patch)).postfix(postfix).build();
    }

    public Version nextMinor() {
        if (minor + 1 > MAX_PATCH_OR_MINOR) {
            throw new IllegalVersionException("minor cannot be increased more than: " + MAX_PATCH_OR_MINOR);
        }
        return builder().prefix(prefix).major(major).minor(nextShort(minor)).patch(DEFAULT_PATCH).postfix(postfix).build();
    }

    public Version previousMinor() {
        if (minor - 1 < MIN_NUMBER) {
            throw new IllegalVersionException("minor cannot be decreased less than: " + MIN_NUMBER);
        }
        return builder().prefix(prefix).major(major).minor(previousShort(minor)).patch(patch).postfix(postfix).build();
    }

    public Version nextMajor() {
        if (major + 1 > MAX_MAJOR) {
            throw new IllegalVersionException("major cannot be increased more than: " + MAX_MAJOR);
        }
        return builder().prefix(prefix).major(major + 1).minor(MIN_NUMBER).patch(DEFAULT_PATCH).postfix(postfix).build();
    }

    public Version previousMajor() {
        if (major - 1 < MIN_NUMBER) {
            throw new IllegalVersionException("major cannot be decreased less than: " + MIN_NUMBER);
        }
        return builder().prefix(prefix).major(major - 1).minor(minor).patch(patch).postfix(postfix).build();
    }

    public Version next() {
        VersionBuilder builder = builder()
                .prefix(prefix).postfix(postfix)
                .major(major).minor(minor).patch(patch);
        if (patch + 1 > MAX_PATCH_OR_MINOR) {
            builder.patch(DEFAULT_PATCH);
            if (minor + 1 > MAX_PATCH_OR_MINOR) {
                builder.minor(DEFAULT_ZERO);
                if (major + 1 > MAX_MAJOR) {
                    throw new IllegalVersionException("MAX version reached");
                }
                return builder.major(major + 1).build();
            } else {
                return builder.minor(nextShort(minor)).build();
            }
        } else {
            return builder.patch(nextShort(patch)).build();
        }
    }

    private static short nextShort(short number) {
        return (short) (number + 1);
    }

    private static short previousShort(short number) {
        return (short) (number - 1);
    }

    public static class VersionBuilder {
        private String prefix = DEFAULT_PREFIX;
        private int major = DEFAULT_ZERO;
        private short minor = DEFAULT_ZERO;
        private short patch = DEFAULT_PATCH;
        private String postfix = DEFAULT_POSTFIX;
    }

}
