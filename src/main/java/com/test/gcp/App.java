package com.test.gcp;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.iam.v1.Iam;
import com.google.api.services.iam.v1.model.ListServiceAccountKeysResponse;
import com.google.api.services.iam.v1.model.ServiceAccountKey;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageException;
import com.google.cloud.storage.StorageOptions;

/**
 * Hello world!
 *
 */
public class App {
	private final static String APP_NAME = "com.test.gcp.GCP_Signing_Keygen_Tests/0.1";

	public static void main(String[] args) throws IOException {
		// GCP project id
		String projectId = "gcp-project12345678";

		// GCP Service account with appropriate permissions
		String serviceAccountName = "sa-account-name";

		List<ServiceAccountKey> keysList = listKeys(projectId, serviceAccountName);

		keysList.forEach(sak -> {
			try {
				System.out.println("\n" + sak.toPrettyString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	// Creates a key for a service account.
	public static List<ServiceAccountKey> listKeys(String projectId, String accountName) {
		String email = String.format("%s@%s.iam.gserviceaccount.com", accountName, projectId);
		String resourcePath = String.format("projects/%s/serviceAccounts/%s", projectId, email);

		// Initialize client that will be used to send requests.
		// This client only needs to be created once, and can be reused for multiple
		// requests.
		try {
			HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

			GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();

			if (credentials.createScopedRequired()) {
				credentials = credentials.createScoped(Arrays.asList("https://www.googleapis.com/auth/cloud-platform"));
			}

			credentials.refreshIfExpired();

			final String access_token = credentials.getAccessToken().getTokenValue();

			Iam iam = new Iam.Builder(httpTransport, jsonFactory, null).setApplicationName(APP_NAME)
					.setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {

						@Override
						public void initialize(AbstractGoogleClientRequest<?> request) throws IOException {

							HttpHeaders httpHeaders = request.getRequestHeaders();
							httpHeaders.put("Authorization", "Bearer " + access_token);
							request.setRequestHeaders(httpHeaders);
						}
					}).build();

			Iam.Projects.ServiceAccounts.Keys.List request = iam.projects().serviceAccounts().keys().list(resourcePath);
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
