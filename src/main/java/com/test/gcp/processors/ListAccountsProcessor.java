package com.test.gcp.processors;

import java.io.IOException;
import java.util.List;

import com.google.api.services.iam.v1.model.ServiceAccountKey;
import com.test.gcp.auth.GCPAccessKeysUtil;
import com.test.gcp.config.DesktopAppConfig;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public class ListAccountsProcessor implements ISigningProcessor {
	@NonNull
	DesktopAppConfig desktopAppConfig;

	@Override
	public void process() {
		System.out.println("Running list keys call.");

		List<ServiceAccountKey> keysList = GCPAccessKeysUtil.listKeys(desktopAppConfig);

		if (keysList.size() > 0) {
			System.out.printf("Found keys for %s: \n", desktopAppConfig.getSaAccountName());

			keysList.forEach(sak -> {
				try {
					System.out.println(sak.toPrettyString());
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		} else {
			System.out.printf("No keys found for: %s\n", desktopAppConfig.getSaAccountName());
		}

	}

	@Override
	public String getSelfDescription() {
		return "ListAccountsProcessor for listing current RSA keys";
	}

}
