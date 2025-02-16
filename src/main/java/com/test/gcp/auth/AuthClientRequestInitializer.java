package com.test.gcp.auth;

import java.io.IOException;

import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.google.api.client.http.HttpHeaders;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor(staticName = "of")
public class AuthClientRequestInitializer implements GoogleClientRequestInitializer {
	@NonNull
	private String accessToken;

	@Override
	public void initialize(AbstractGoogleClientRequest<?> request) throws IOException {

		HttpHeaders httpHeaders = request.getRequestHeaders();
		httpHeaders.put("Authorization", "Bearer " + accessToken);
		request.setRequestHeaders(httpHeaders);
	}

}
