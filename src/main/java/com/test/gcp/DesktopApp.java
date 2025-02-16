package com.test.gcp;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageException;
import com.google.cloud.storage.StorageOptions;
import com.test.gcp.config.ConfigLoader;
import com.test.gcp.config.DesktopAppConfig;
import com.test.gcp.processors.ISigningProcessor;
import com.test.gcp.processors.ProcessorFactory;

public class DesktopApp {
	private final static String DESKTOP_APP_CONFIG = "./configs/DesktopApp.yaml";
	static int curProcessorRunCount = 0;

	public static void main(String[] args) throws IOException {
		DesktopAppConfig desktopAppConfig = ConfigLoader.loadBasicYAMLConfig(DESKTOP_APP_CONFIG,
				DesktopAppConfig.class);

		desktopAppConfig.getSigningOperations().forEach(curSigningOp -> {
			ISigningProcessor signingProcessor = ProcessorFactory.getSigningProcessor(curSigningOp, desktopAppConfig);

			System.out.printf("Starting operation #%d as %s%n", curProcessorRunCount,
					signingProcessor.getSelfDescription());

			signingProcessor.process(desktopAppConfig);

			System.out.printf("Completed operation #%d as %s%n", curProcessorRunCount,
					signingProcessor.getSelfDescription());

			curProcessorRunCount++;
		});

	}

	// Creates a key for a service account.

	/**
	 * Signing a URL requires Credentials which implement ServiceAccountSigner.
	 * These can be set explicitly using the
	 * Storage.SignUrlOption.signWith(ServiceAccountSigner) option. If you don't,
	 * you could also pass a service account signer to StorageOptions, i.e.
	 * StorageOptions().newBuilder().setCredentials(ServiceAccountSignerCredentials).
	 * In this example, neither of these options are used, which means the following
	 * code only works when the credentials are defined via the environment variable
	 * GOOGLE_APPLICATION_CREDENTIALS, and those credentials are authorized to sign
	 * a URL. See the documentation for Storage.signUrl for more details.
	 */
	public static void generateV4GetObjectSignedUrl(String projectId, String bucketName, String objectName)
			throws StorageException {

//		  Storage.SignUrlOption.signWith(ServiceAccountSigner);
//		  ServiceAccountCredentials.from
//		  
//		  Storage storage =
//				    StorageOptions.newBuilder(new ServiceAccountKey())
//				        .setCredentials())
//				        .build()
//				        .getService();

		Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();

		// Define resource
		BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.of(bucketName, objectName)).build();

		URL url = storage.signUrl(blobInfo, 15, TimeUnit.MINUTES, Storage.SignUrlOption.withV4Signature());

		System.out.println("Generated GET signed URL:");
		System.out.println(url);
		System.out.println("You can use this URL with any user agent, for example:");
		System.out.println("curl '" + url + "'");
	}
}
