/*
 * Copyright 2011 Henry Coles
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package org.pitest.mutationtest.tooling;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;

public class DirectorySourceLocatorTest {

  private DirectorySourceLocator testee;
  private Path root;

  @Mock
  Function<Path, Optional<Reader>>        locator;

  @Before
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    this.root = Paths.get(".");
    this.testee = new DirectorySourceLocator(this.root, this.locator);
    when(this.locator.apply(any(Path.class)))
    .thenReturn(Optional.<Reader> empty());
  }

  @Test
  public void shouldLocateSourceForClassesInDefaultPackage() {
    this.testee.locate(Collections.singletonList("Foo"), "Foo.java");
    Path expected = root.resolve("Foo.java");
    verify(this.locator).apply(expected);
  }

  @Test
  public void shouldLocateSourceForClassesInNamedPackages() {
    this.testee
    .locate(Collections.singletonList("com.example.Foo"), "Foo.java");
    Path expected = root.resolve("com").resolve("example").resolve("Foo.java");
    verify(this.locator).apply(expected);
  }
}
