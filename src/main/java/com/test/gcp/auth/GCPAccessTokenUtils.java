package com.test.gcp.auth;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

import com.google.auth.oauth2.GoogleCredentials;
import com.test.gcp.config.DesktopAppConfig;

public class GCPAccessTokenUtils {
	private GCPAccessTokenUtils() {}
	

	public static String genGCPAccessToken(DesktopAppConfig desktopAppConfig) {
		GoogleCredentials credentials;

		try {
			// Load config for access token to be provided to GCP IAM Credentials API
			if (desktopAppConfig.getAccessTokenPath() != null) {
				credentials = GoogleCredentials.fromStream(new FileInputStream(desktopAppConfig.getAccessTokenPath()));
			} else {
				credentials = GoogleCredentials.getApplicationDefault();
			}

			if (credentials.createScopedRequired()) {
				credentials = credentials.createScoped(Arrays.asList("https://www.googleapis.com/auth/cloud-platform"));
			}

			credentials.refreshIfExpired();

			return credentials.getAccessToken().getTokenValue();
		} catch (IOException ex) {
			throw new RuntimeException("Failed to setup GoogleCredentials to make requests from GCP IAM API.", ex);
		}
	}

}
