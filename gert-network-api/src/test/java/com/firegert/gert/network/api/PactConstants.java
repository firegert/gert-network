package com.firegert.gert.network.api;

public final class PactConstants {

    public static final String PACT_FOLDER = "pacts";
    public static final String PACT_APP_NAME = "GERT-NETWORK";
    private static final String DOT = ".";

    private PactConstants() {
    }

    public static final class Provider {

        public static final String DEMO_API_PROVIDER = PACT_APP_NAME + DOT + "DEMO-API";

        private Provider() {
        }
    }

}
