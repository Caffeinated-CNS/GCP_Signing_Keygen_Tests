package com.test.gcp.processors;

import com.test.gcp.config.DesktopAppConfig;
import com.test.gcp.config.OperationMode;
import com.test.gcp.config.SigningOperation;

public class ProcessorFactory {
	private final static EmptyProcessor NOT_IMPLEMENTED = new EmptyProcessor(DesktopAppConfig.of(),
			OperationMode.DEACTIVATE_HMAC_KEY);

	private ProcessorFactory() {
	}

	public static ISigningProcessor getSigningProcessor(SigningOperation appMode, DesktopAppConfig desktopAppConfig) {

		switch (appMode.toOperationMode()) {
		case LIST_RSA_KEYS:
			return new ListAccountsProcessor(desktopAppConfig);
		case LIST_HMAC_KEYS:
			return NOT_IMPLEMENTED;
		case GEN_RSA_KEY:
			return NOT_IMPLEMENTED;
		case GEN_HMAC_KEY:
			return NOT_IMPLEMENTED;
		case DEACTIVATE_RSA_KEY:
			return NOT_IMPLEMENTED;
		case DEACTIVATE_HMAC_KEY:
			return NOT_IMPLEMENTED;
		case RSA_LOCAL_SIGN_URL:
			return new RSASignedGCSURLProcessor(desktopAppConfig, appMode);
		case HMAC_LOCAL_SIGN_URL:
			return NOT_IMPLEMENTED;
		case RSA_REMOTE_SIGN_URL:
			return NOT_IMPLEMENTED;
		case HMAC_REMOTE_SIGN_URL:
			return NOT_IMPLEMENTED;
		default:
			return NOT_IMPLEMENTED;
		}

//		throw new RuntimeException("Failed to find operation for provided SigningOperation config string.");
	}

}
