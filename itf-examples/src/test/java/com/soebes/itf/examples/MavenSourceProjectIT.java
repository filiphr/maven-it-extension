package com.soebes.itf.examples;

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

import static com.soebes.itf.extension.assertj.MavenITAssertions.assertThat;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;

import com.soebes.itf.jupiter.extension.MavenGoal;
import com.soebes.itf.jupiter.extension.MavenJupiterExtension;
import com.soebes.itf.jupiter.extension.MavenOption;
import com.soebes.itf.jupiter.extension.MavenProject;
import com.soebes.itf.jupiter.extension.MavenSourceProject;
import com.soebes.itf.jupiter.extension.MavenTest;
import com.soebes.itf.jupiter.extension.SystemProperty;
import com.soebes.itf.jupiter.maven.MavenExecutionResult;

@MavenJupiterExtension
@MavenSourceProject("com/soebes/itf/examples/source/class-level")
class MavenSourceProjectIT {

  @MavenTest
  @MavenGoal("help:evaluate")
  @SystemProperty(value = "expression", content = "class.level")
  void class_level_source_project(MavenExecutionResult result) {
    assertThat(result)
            .isSuccessful()
            .out()
            .plain()
            .contains("default-class-property");
  }

  @MavenTest
  @MavenGoal("help:evaluate")
  @SystemProperty(value = "expression", content = "class.level")
  @SystemProperty(value = "class.level", content = "test-property")
  void class_level_source_project_custom_value(MavenExecutionResult result) {
    assertThat(result)
            .isSuccessful()
            .out()
            .plain()
            .contains("test-property");
  }

  @MavenTest
  @MavenGoal("help:evaluate")
  @SystemProperty(value = "expression", content = "method.level")
  @MavenSourceProject("com/soebes/itf/examples/source/method-level")
  void method_level_source_project(MavenExecutionResult result) {
    assertThat(result)
            .isSuccessful()
            .out()
            .plain()
            .contains("default-method-property");
  }

  @MavenTest
  @MavenGoal("help:evaluate")
  @SystemProperty(value = "expression", content = "method.level")
  @SystemProperty(value = "method.level", content = "test-property")
  @MavenSourceProject("com/soebes/itf/examples/source/method-level")
  void method_level_source_project_custom_value(MavenExecutionResult result) {
    assertThat(result)
            .isSuccessful()
            .out()
            .plain()
            .contains("test-property");
  }

  @Nested
  @MavenSourceProject("com/soebes/itf/examples/source/nested-class-level")
  class NestedExample {

    @MavenTest
    @MavenGoal("help:evaluate")
    @SystemProperty(value = "expression", content = "nested.class.level")
    void class_level_source_project(MavenExecutionResult result) {
      assertThat(result)
              .isSuccessful()
              .out()
              .plain()
              .contains("default-nested-class-property");
    }

    @MavenTest
    @MavenGoal("help:evaluate")
    @SystemProperty(value = "expression", content = "nested.class.level")
    @SystemProperty(value = "nested.class.level", content = "test-property")
    void class_level_source_project_custom_value(MavenExecutionResult result) {
      assertThat(result)
              .isSuccessful()
              .out()
              .plain()
              .contains("test-property");
    }

    @MavenTest
    @MavenGoal("help:evaluate")
    @SystemProperty(value = "expression", content = "method.level")
    @MavenSourceProject("com/soebes/itf/examples/source/method-level")
    void method_level_source_project(MavenExecutionResult result) {
      assertThat(result)
              .isSuccessful()
              .out()
              .plain()
              .contains("default-method-property");
    }

    @MavenTest
    @MavenGoal("help:evaluate")
    @SystemProperty(value = "expression", content = "method.level")
    @SystemProperty(value = "method.level", content = "test-property")
    @MavenSourceProject("com/soebes/itf/examples/source/method-level")
    void method_level_source_project_custom_value(MavenExecutionResult result) {
      assertThat(result)
              .isSuccessful()
              .out()
              .plain()
              .contains("test-property");
    }

  }

}
