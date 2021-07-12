package org.arl.commons.logging.jul;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * JUL logging configurator.
 * <p>
 * If the environment variable JUL_CONFIG_FILE is specified, and the file referred to by that path exists, logging
 * properties are read from that file. Otherwise, attempt configuration from the resource named 'logging.properties'.
 * </p>
 */
public class LogManager {

  /**
   * Constructs a new instance of CustomLogManager.
   */
  public LogManager() {
    super();

    init();
  }

  private void init() {
    final String configFilePath = System.getenv("JUL_CONFIG_FILE");
    if (configFilePath != null) {
      final File configFile = new File(configFilePath);
      if (configFile.isFile()) {
        try (final InputStream inputStream = new FileInputStream(configFile)) {
          java.util.logging.LogManager.getLogManager().readConfiguration(inputStream);
          return;
        } catch (IOException e) {
          // ignore exception
        }
      }
    }
    try (final InputStream inputStream = Thread.currentThread().getContextClassLoader()
        .getResourceAsStream("logging.properties")) {
      if (inputStream != null) {
        java.util.logging.LogManager.getLogManager().readConfiguration(inputStream);
        return;
      }
    } catch (IOException e) {
      // ignore exception
    }
  }
}
