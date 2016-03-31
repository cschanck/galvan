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
package org.terracotta.testing.logging;

import java.io.PrintStream;


/**
 * Meant to provide a very simple mechanism for logging verbose data, throughout the system.
 * If the instance is initialized with a null output PrintStream, it will assume that it is to run in "silent" mode and will
 * log nothing to output.
 */
public class VerboseLogger implements ILogger {
  private final PrintStream output;
  private final PrintStream fatal;
  
  public VerboseLogger(PrintStream output, PrintStream fatal) {
    this.output = output;
    this.fatal = fatal;
  }

  @Override
  public void log(String message) {
    if (null != this.output) {
      this.output.println(message);
    }
  }

  @Override
  public void fatal(String message) {
    if (null != this.fatal) {
      this.fatal.println(message);
    }
  }
}
