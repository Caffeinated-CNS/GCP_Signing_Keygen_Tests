package com.test.gcp.config;

public enum OperationMode {
	LIST_RSA_KEYS("LIST_RSA_KEYS"), 
	LIST_HMAC_KEYS("LIST_HMAC_KEYS"), 
	GEN_RSA_KEY("GEN_RSA_KEY"),
	GEN_HMAC_KEY("GEN_HMAC_KEY"), 
	DEACTIVATE_RSA_KEY("DEACTIVATE_RSA_KEY"),
	DEACTIVATE_HMAC_KEY("DEACTIVATE_HMAC_KEY"), 
	RSA_LOCAL_SIGN_URL("RSA_LOCAL_SIGN_URL"),
	HMAC_LOCAL_SIGN_URL("HMAC_LOCAL_SIGN_URL"), 
	RSA_REMOTE_SIGN_URL("RSA_REMOTE_SIGN_URL"),
	HMAC_REMOTE_SIGN_URL("HMAC_REMOTE_SIGN_URL");

	private final String configString;

	OperationMode(final String configString) {
		this.configString = configString;
	}

	@Override
	public String toString() {
		return configString;
	}

	public static OperationMode toOperationMode(String operationMode) {
		for (OperationMode curOpMode : OperationMode.values()) {
			if (operationMode.equalsIgnoreCase(curOpMode.toString())) {
				return curOpMode;
			}
		}

		throw new RuntimeException("Failed to decode configured SigningOperation config setting.");
	}

	public static boolean isMatchEnumValue(String compareStr) {
		if (compareStr != null && !compareStr.isEmpty()) {
			for (OperationMode curOpMode : OperationMode.values()) {
				if (compareStr.equalsIgnoreCase(curOpMode.toString())) {
					return true;
				}
			}
		}
		return false;
	}
}
