package com.test.gcp;

import java.io.IOException;

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

			signingProcessor.process();

			System.out.printf("Completed operation #%d as %s%n", curProcessorRunCount,
					signingProcessor.getSelfDescription());

			curProcessorRunCount++;
		});

	}
}
