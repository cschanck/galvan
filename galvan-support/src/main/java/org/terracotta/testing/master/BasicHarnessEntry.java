/*
 * Copyright Terracotta, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terracotta.testing.master;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.terracotta.testing.api.BasicTestClusterConfiguration;
import org.terracotta.testing.common.Assert;
import org.terracotta.testing.logging.ContextualLogger;
import org.terracotta.testing.logging.VerboseLogger;


public class BasicHarnessEntry extends AbstractHarnessEntry<BasicTestClusterConfiguration> {
  // Run the one configuration.
  protected boolean runOneConfiguration(VerboseLogger logger, ContextualLogger fileHelperLogger, String kitOriginPath, String configTestDirectory, String clientClassPath, DebugOptions debugOptions, int clientsToCreate, String testClassName, boolean isRestartable, List<String> extraJarPaths, String namespaceFragment, String serviceFragment, BasicTestClusterConfiguration runConfiguration) throws IOException, FileNotFoundException, InterruptedException {
    int serversToCreate = runConfiguration.serversInStripe;
    return runOneBasicConfiguration(logger, fileHelperLogger, kitOriginPath, configTestDirectory, clientClassPath, debugOptions, serversToCreate, clientsToCreate, testClassName, isRestartable, extraJarPaths, namespaceFragment, serviceFragment);
  }

  // A single-active stripe.
  private boolean runOneBasicConfiguration(VerboseLogger logger, ContextualLogger fileHelperLogger, String serverInstallDirectory, String testParentDirectory, String clientClassPath, DebugOptions debugOptions, int serversPerStripe, int clientsToCreate, String testClassName, boolean isRestartable, List<String> extraJarPaths, String namespaceFragment, String serviceFragment) throws IOException, FileNotFoundException, InterruptedException {
    Assert.assertTrue(serversPerStripe > 0);
    
    // This is the simple case of a single-stripe so we don't need to wrap or decode anything.
    String stripeName = "stripe" + 0;
    ReadyStripe oneStripe = CommonIdioms.setupConfigureAndStartStripe(logger, fileHelperLogger, serverInstallDirectory, testParentDirectory, serversPerStripe, SERVER_START_PORT, 0, isRestartable, extraJarPaths, namespaceFragment, serviceFragment, stripeName);
    // We just want to unwrap this, directly.
    IMultiProcessControl processControl = oneStripe.stripeControl;
    String connectUri = oneStripe.stripeUri;
    Assert.assertTrue(null != processControl);
    Assert.assertTrue(null != connectUri);
    
    // The cluster is now running so install and run the clients.
    boolean wasCompleteSuccess = CommonIdioms.installAndRunClients(logger, testParentDirectory, clientClassPath, debugOptions, clientsToCreate, testClassName, processControl, connectUri);
    // Stop all the servers.
    processControl.shutDown();
    return wasCompleteSuccess;
  }
}
