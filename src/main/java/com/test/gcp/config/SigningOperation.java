package com.test.gcp.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SigningOperation {
	@NonNull
	private String operationMode;
	private String gcsBucket = null;
	private String gcsObjectPath = null;

	public enum OperationModes {
		LIST_KEYS("LIST_KEYS"),
		LIST_HMAC_KAYS("LIST_HMAC_KAYS"),
		GEN_RSA_KEY("GEN_RSA_KEY"),
		GEN_HMAC_KEY("GEN_HMAC_KEY"),
		DEACTIVATE_RSA_KEY("DEACTIVATE_RSA_KEY"),
		DEACTIVATE_HMAC_KEY("DEACTIVATE_HMAC_KEY");

		private final String configString;

		OperationModes(final String configString) {
			this.configString = configString;
		}

		@Override
		public String toString() {
			return configString;
		}
	};

	public OperationModes toOperationMode() {
		if (operationMode.equalsIgnoreCase(OperationModes.LIST_KEYS.toString())) {
			return OperationModes.LIST_KEYS;
		}

		throw new RuntimeException("Failed to decode configured SigningOperation config setting.");
	}

	public static boolean isMatchEnumValue(String compareStr) {
		if (compareStr != null && !compareStr.isEmpty()) {
			for (OperationModes curAppMode : OperationModes.values()) {
				if (compareStr.equalsIgnoreCase(curAppMode.toString())) {
					return true;
				}
			}
		}
		return false;
	}

}
