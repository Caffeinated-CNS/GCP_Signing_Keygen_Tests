package com.test.gcp.config;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor(staticName="of")
public class DesktopAppConfig {

	// GCP project id to query against.
	@NonNull
	private String gcpProjectId;
	// GCP Service account with appropriate permissions.
	@NonNull
	private String saAccountName;
	private String accessTokenPath = null;

	@NonNull
	private List<SigningOperation> signingOperations = new ArrayList<>();
	private String appName = "com.test.gcp.GCP_Signing_Keygen_Tests/0.1";

}
