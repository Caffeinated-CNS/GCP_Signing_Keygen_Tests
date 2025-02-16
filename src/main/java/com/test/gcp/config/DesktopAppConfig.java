package com.test.gcp.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class DesktopAppConfig {
	// GCP project id to query against.
	@NonNull
	private String gcpProjectId;
	// GCP Service account with appropriate permissions.
	@NonNull
	private String saAccountName;
	private String accessTokenPath = null;

}
