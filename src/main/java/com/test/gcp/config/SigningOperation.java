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

	public enum OperationMode {
		LIST_KEYS("LIST_KEYS"), 
		LIST_HMAC_KEYS("LIST_HMAC_KAYS"), 
		GEN_RSA_KEY("GEN_RSA_KEY"),
		GEN_HMAC_KEY("GEN_HMAC_KEY"), 
		DEACTIVATE_RSA_KEY("DEACTIVATE_RSA_KEY"),
		DEACTIVATE_HMAC_KEY("DEACTIVATE_HMAC_KEY");

		private final String configString;

		OperationMode(final String configString) {
			this.configString = configString;
		}

		@Override
		public String toString() {
			return configString;
		}
	};

	public OperationMode toOperationMode() {
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
