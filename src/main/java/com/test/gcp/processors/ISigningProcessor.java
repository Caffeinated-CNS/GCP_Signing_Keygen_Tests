/**
 * 
 */
package com.test.gcp.processors;

import com.test.gcp.config.DesktopAppConfig;

/**
 * 
 */
public interface ISigningProcessor {
	
	public void process(DesktopAppConfig desktopAppConfig);
	
	public String getSelfDescription();

}
