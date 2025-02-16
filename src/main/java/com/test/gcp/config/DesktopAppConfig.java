package com.test.gcp.config;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DesktopAppConfig {
	// GCP project id to query against.
	private String gcpProjectId;
	// GCP Service account with appropriate permissions.
	private String saAccountName;

}
