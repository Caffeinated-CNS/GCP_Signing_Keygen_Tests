package com.test.gcp.processors;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.test.gcp.config.DesktopAppConfig;
import com.test.gcp.config.SigningOperation;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public class RSASignedGCSURLProcessor implements ISigningProcessor {
	@NonNull
	private DesktopAppConfig desktopAppConfig;
	@NonNull
	private SigningOperation signingOperation;

	@Override
	public void process() {

		if (signingOperation.getRsaKeyID() == null) {
			throw new RuntimeException(
					"Java libraries do not currently support signing URLs without a service account key.");
		}

//		GoogleCredentials googleCredentials = GoogleCredentials.create(GCPAccessTokenUtils.genGCPAccessToken(desktopAppConfig));

		ServiceAccountCredentials serviceAccountCredentials;
		try {
			serviceAccountCredentials = ServiceAccountCredentials
					.fromStream(new FileInputStream(desktopAppConfig.getAccessTokenPath()));
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to load service account key file.", e);
		}

		StorageOptions storageOptions = StorageOptions.newBuilder().setProjectId(desktopAppConfig.getGcpProjectId())
				.build();
//				.setCredentials(serviceAccountCredentials).build();

		// Define resource
		BlobInfo blobInfo = BlobInfo
				.newBuilder(BlobId.of(signingOperation.getGcsBucket(), signingOperation.getGcsObjectPath())).build();

		URL url = storageOptions.getService().signUrl(blobInfo, signingOperation.getTimeoutInMins(), TimeUnit.MINUTES,
				Storage.SignUrlOption.withV4Signature(), 
				Storage.SignUrlOption.signWith(serviceAccountCredentials),
				Storage.SignUrlOption.withQueryParams(getExtendedQueryParams()),
				Storage.SignUrlOption.withExtHeaders(getExtendedHeaders()));

		System.out.println("Generated GET signed URL:");
		System.out.println(url);
		System.out.println("You can use this URL with any user agent, for example:");
		System.out.println("curl '" + url + "'");
	}

	private HashMap<String, String> getExtendedQueryParams() {
		HashMap<String, String> extendedQueryParams = new HashMap<>();
		
		extendedQueryParams.put("Consumer-Name", "client_asdasdasd_1234");
		extendedQueryParams.put("Extra-Log-Info", "some thing for log discovery");
		extendedQueryParams.put("Generator-Service", desktopAppConfig.getAppName());
		
		return extendedQueryParams;
	}
	
	/*
	 * These are not persisted into the Cloud Storage event queue, i.e., printing
	 * the CloudEvent received at Cloud Run Functions does not include these
	 * headers.
	 */
	private HashMap<String, String> getExtendedHeaders() {
		HashMap<String, String> extendedHeaders = new HashMap<>();

//		extendedHeaders.put("Consumer-Name", "client_asdasdasd_1234");
//		extendedHeaders.put("Extra-Log-Info", "some thing for log discovery");
//		extendedHeaders.put("Generator-Service", desktopAppConfig.getAppName());

		return extendedHeaders;
	}

	@Override
	public String getSelfDescription() {
		return "GenRSAKeyProcessor for generating a new RSA Service Account key.";
	}
}
