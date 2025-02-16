package com.test.gcp.auth;

import java.util.List;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.iam.v1.Iam;
import com.google.api.services.iam.v1.Iam.Projects.ServiceAccounts.Keys;
import com.google.api.services.iam.v1.model.ListServiceAccountKeysResponse;
import com.google.api.services.iam.v1.model.ServiceAccountKey;
import com.test.gcp.config.DesktopAppConfig;

public class GCPAccessKeysUtil {
	private GCPAccessKeysUtil() {
	}

	public static List<ServiceAccountKey> listKeys(DesktopAppConfig desktopAppConfig) {
		String email = String.format("%s@%s.iam.gserviceaccount.com", desktopAppConfig.getSaAccountName(),
				desktopAppConfig.getGcpProjectId());
		String resourcePath = String.format("projects/%s/serviceAccounts/%s", desktopAppConfig.getGcpProjectId(),
				email);

		// Initialize client that will be used to send requests.
		// This client only needs to be created once, and can be reused for multiple
		// requests.
		try {
			HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
			final String access_token = GCPAccessTokenUtils.genGCPAccessToken(desktopAppConfig);

			Iam iam = new Iam.Builder(httpTransport, jsonFactory, null)
					.setApplicationName(desktopAppConfig.getAppName())
					.setGoogleClientRequestInitializer(AuthClientRequestInitializer.of(access_token)).build();

			Keys.List request = iam.projects().serviceAccounts().keys().list(resourcePath);
			ListServiceAccountKeysResponse response = request.execute();

			return response.getKeys();
//			ServiceAccountKey sak = iam.projects().serviceAccounts().keys().get(resourcePath + "/keys").execute();
//
//			System.out.println("\n\n" + sak.toPrettyString());
//			
//			return null;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}

}
