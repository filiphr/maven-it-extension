package com.soebes.itf.examples.repository;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import com.soebes.itf.jupiter.extension.MavenJupiterExtension;
import com.soebes.itf.jupiter.extension.MavenRepository;
import com.soebes.itf.jupiter.extension.MavenTest;
import com.soebes.itf.jupiter.maven.MavenProjectResult;
import org.junit.jupiter.api.Nested;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

@MavenJupiterExtension
@MavenRepository
class MavenRepositoryNestedIT {

  @MavenTest
  void repository_in_class_directory(MavenProjectResult result) {
    File cacheDirectory = result.getTargetCacheDirectory();
    File expected = new File("target/maven-it/com/soebes/itf/examples/repository/MavenRepositoryNestedIT/.m2/repository");
    assertThat(cacheDirectory.getAbsoluteFile()).isEqualTo(expected.getAbsoluteFile());
  }

  @Nested
  class NestedIT {

    @MavenTest
    void repository_in_enclosing_class_directory(MavenProjectResult result) {
      File cacheDirectory = result.getTargetCacheDirectory();
      File expected = new File("target/maven-it/com/soebes/itf/examples/repository/MavenRepositoryNestedIT/.m2/repository");
      assertThat(cacheDirectory.getAbsoluteFile()).isEqualTo(expected.getAbsoluteFile());
    }
  }
}
