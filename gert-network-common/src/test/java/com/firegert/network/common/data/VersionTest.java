package com.firegert.network.common.data;

import com.firegert.network.common.exception.IllegalVersionException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class VersionTest {

    public static final String V_0_0_1_SNAPSHOT = "V-0.0.1-SNAPSHOT";

    @Test
    void testDefaultVersions() {
        assertAll(
                () -> assertEquals(V_0_0_1_SNAPSHOT, Version.DEFAULT_VERSION.toString()),
                () -> assertEquals(V_0_0_1_SNAPSHOT, new Version().toString()),
                () -> assertEquals(V_0_0_1_SNAPSHOT, Version.builder().build().toString()));
    }

    @Test
    void testVersionEquality() {
        assertAll(
                () -> assertEquals(new Version(), new Version()),
                () -> assertEquals(new Version(1), new Version(1)),
                () -> assertEquals(new Version(1, (short) 1), new Version(1, (short) 1)),
                () -> assertEquals(new Version(1, (short) 1, (short) 1), new Version(1, (short) 1, (short) 1)),
                () -> assertEquals(new Version(), Version.builder().build()),
                () -> assertEquals(new Version(1), Version.builder().major(1).build()),
                () -> assertEquals(new Version(1, (short) 1), Version.builder().major(1).minor((short) 1).build()),
                () -> assertEquals(new Version(1, (short) 1, (short) 1),
                        Version.builder().major(1).minor((short) 1).patch((short) 1).build()),
                () -> assertEquals(Version.DEFAULT_VERSION, new Version()),
                () -> assertEquals(Version.DEFAULT_VERSION.next(), new Version().next()),
                () -> assertEquals(Version.DEFAULT_VERSION.nextPatch(), new Version().nextPatch()),
                () -> assertEquals(new Version("A", 1, (short) 1, (short) 1, "Z"),
                        Version.builder().prefix("a").major(1).minor((short) 1).patch((short) 1).postfix("Z").build())
        );
    }


    @ParameterizedTest
    @ArgumentsSource(ValidVersionPartArgProvider.class)
    public void testValidVersionCreationFromParts(String prefix, int major, short minor, short patch,
                                                  String postfix, String expected) {
        Version version = Version.builder().prefix(prefix).major(major).minor(minor).patch(patch).postfix(postfix).build();
        assertEquals(expected, version.toString());
    }

    @ParameterizedTest
    @ArgumentsSource(ValidVersionTextArgProvider.class)
    public void testValidVersionCreationFromText(String versionText, String prefix, int major, short minor, short patch
            , String postfix) {
        Version version = Version.fromString(versionText);
        assertAll(() -> assertEquals(prefix, version.prefix()),
                () -> assertEquals(major, version.major()),
                () -> assertEquals(minor, version.minor()),
                () -> assertEquals(patch, version.patch()),
                () -> assertEquals(postfix, version.postfix()));
    }

    @ParameterizedTest
    @ArgumentsSource(InvalidVersionTextArgProvider.class)
    public void testInvalidVersionCreationFromText(String versionText) {
        assertThrows(IllegalVersionException.class, () -> Version.fromString(versionText));
    }

    @ParameterizedTest
    @ArgumentsSource(InvalidVersionPartArgProvider.class)
    public void testInvalidVersionCreationFromParts(String prefix, int major, short minor, short patch
            , String postfix) {
        assertAll(
                () -> assertThrows(IllegalVersionException.class, () -> new Version(prefix, major, minor, patch,
                        postfix)),
                () -> assertThrows(IllegalVersionException.class,
                        () -> Version.builder().prefix(prefix).major(major).minor(minor).patch(patch).postfix(postfix).build()));
    }

    @ParameterizedTest
    @ArgumentsSource(NextValidVersionArgProvider.class)
    void testNextValidVersion(String expectedNext, Version current) {
        assertAll(
                () -> assertEquals(expectedNext, current.next().toString())
        );
    }

    @Test
    void testNextPreviousVersionUpdate() {
        assertAll(
                () -> assertEquals("A-0.0.2-Z", Version.fromString("A-0.0.1-Z").next().toString()),
                () -> assertEquals("A-0.0.101-Z", Version.fromString("A-0.0.100-Z").nextPatch().toString()),
                () -> assertEquals("A-0.0.99-Z", Version.fromString("A-0.0.100-Z").previousPatch().toString()),
                () -> assertThrows(IllegalVersionException.class,
                        () -> Version.fromString("A-0.0.1-Z").previousPatch().toString()),
                () -> assertEquals("A-0.1.1-Z", Version.fromString("A-0.0.100-Z").nextMinor().toString()),
                () -> assertEquals("A-0.99.100-Z", Version.fromString("A-0.100.100-Z").previousMinor().toString()),
                () -> assertThrows(IllegalVersionException.class,
                        () -> Version.fromString("A-0.0.1-Z").previousMinor().toString()),
                () -> assertEquals("A-1.0.1-Z", Version.fromString("A-0.200.100-Z").nextMajor().toString()),
                () -> assertEquals("A-99.0.1-Z", Version.fromString("A-100.0.1-Z").previousMajor().toString()),
                () -> assertThrows(IllegalVersionException.class,
                        () -> Version.fromString("A-0.0.1-Z").previousMajor().toString())
        );
    }


    // Arg providers
    static class NextValidVersionArgProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(
                    Arguments.of("A-0.0.2-Z", Version.builder().prefix("A").postfix("Z").build()),
                    Arguments.of("A-0.2.2-Z", Version.fromString("A-0.2.1-Z")),
                    Arguments.of("A-0.1.1-Z", Version.fromString("A-0.0.32766-Z")),
                    Arguments.of("A-1.0.1-Z", Version.fromString("A-0.32766.32766-Z")),
                    Arguments.of("A-2.0.1-Z", Version.fromString("A-1.32766.32766-Z")),
                    Arguments.of("A-1.32766.2-Z", Version.fromString("A-1.32766.1-Z")),
                    Arguments.of("A-1.32766.2-Z", Version.fromString("A-1.32766.1-Z"))
            );
        }
    }
    // 2147483647

    static class ValidVersionPartArgProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(Arguments.of("AAA", 1, (short) 2, (short) 3, "ZZZ", "AAA-1.2.3-ZZZ"),
                    Arguments.of("aaa", 1, (short) 2, (short) 3, "ZZZ", "aaa-1.2.3-ZZZ"),
                    Arguments.of("X", 1, (short) 2, (short) 3, "zzzzzzzz", "X-1.2.3-zzzzzzzz"));
        }
    }

    static class InvalidVersionPartArgProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(Arguments.of("AAA", Integer.MAX_VALUE, (short) 2, (short) 3, "kkk"),
                    Arguments.of("aaa", 1, Short.MAX_VALUE, (short) 3, "ZZZ"),
                    Arguments.of("any", 1, (short) 2, Short.MAX_VALUE, "any"),
                    Arguments.of("any", -1, (short) 2, (short) 3, "any"),
                    Arguments.of("any", 1, (short) -2, (short) 3, "any"),
                    Arguments.of("any", 1, (short) 2, (short) -3, "any"),
                    Arguments.of(null, Integer.MAX_VALUE, Short.MAX_VALUE, Short.MAX_VALUE, ""));
        }
    }

    static class ValidVersionTextArgProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(Arguments.of("AAA-1.2.3-ZZZ", "AAA", 1, (short) 2, (short) 3, "ZZZ"),
                    Arguments.of("aaa-1.2.3-ZZZ", "aaa", 1, (short) 2, (short) 3, "ZZZ"),
                    Arguments.of("X-1.2.3-zzzzzzzz", "X", 1, (short) 2, (short) 3, "zzzzzzzz"));
        }
    }

    static class InvalidVersionTextArgProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(
                    Arguments.of("-..-"),
                    Arguments.of("some random text"),
                    Arguments.of("onlyPrefix"),
                    Arguments.of("prefix-0"),
                    Arguments.of("prefix-0.1"),
                    Arguments.of("prefix-0.1.2"),
                    Arguments.of("prefix-0.1.2.3"),
                    Arguments.of("0.4.4-postfix"),
                    Arguments.of("-0.4.4-postfix"),
                    Arguments.of("prefix044postfix"),
                    Arguments.of("prefix_0*4+4,postfix"),
                    Arguments.of("prefix-A0.4.4-postfix"),
                    Arguments.of("prefix-A.b.c-postfix"),
                    Arguments.of("prefix-..-postfix"));
        }
    }
}
