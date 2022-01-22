package com.soebes.itf.jupiter.extension;

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

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Karl Heinz Marbaise
 */
class AnnotationHelper {

  private AnnotationHelper() {
    // prevent instantiation.
  }

  /**
   * @param context {@link ExtensionContext}
   * @return {@code true} if we have any {@link MavenProfile @MavenProfile} defined or {@code false} otherwise.
   */
  static boolean hasProfiles(ExtensionContext context) {
    return profiles(context).size() > 0;
  }

  /**
   * Get the profiles from the annotation.
   *
   * @param context {@link ExtensionContext}
   * @return The stream with the profiles if exist.
   */
  static List<String> profiles(ExtensionContext context) {
    //MavenProfile[] annotationsByType = context.getClass().getPackage().getAnnotationsByType(MavenProfile.class);
    List<MavenProfile> mavenProfilesOnTestMethod = AnnotationSupport.findRepeatableAnnotations(context.getTestMethod(), MavenProfile.class);
    List<String> profiles = mavenProfilesOnTestMethod.stream().flatMap(profile -> Stream.of(profile.value())).collect(Collectors.toList());
    if (profiles.size() > 0) {
      return profiles;
    }

    List<MavenProfile> repeatableAnnotationsOnClass = AnnotationSupport.findRepeatableAnnotations(context.getTestClass(), MavenProfile.class);
    return repeatableAnnotationsOnClass.stream().flatMap(profile -> Stream.of(profile.value())).collect(Collectors.toList());
  }

  /**
   * @param context {@link ExtensionContext}
   * @return {@code true} if we have any {@link MavenGoal @MavenGoal} defined or {@code false} otherwise.
   */
  static boolean hasGoals(ExtensionContext context) {
    return goals(context).size() > 0;
  }

  /**
   * Get the goals from the annotation either on test method level
   * or on test class level.
   *
   * @param context {@link ExtensionContext}
   * @return The stream with the goals if exist.
   */
  static List<String> goals(ExtensionContext context) {
    List<MavenGoal> goalAnnotations = AnnotationSupport.findRepeatableAnnotations(context.getTestMethod(), MavenGoal.class);
    List<String> stringStream = goalAnnotations.stream().flatMap(goal -> Stream.of(goal.value())).collect(Collectors.toList());
    if (stringStream.size() > 0) {
      return stringStream;
    }

    List<MavenGoal> repeatableAnnotationsOnClass = AnnotationSupport.findRepeatableAnnotations(context.getTestClass(), MavenGoal.class);
    return repeatableAnnotationsOnClass.stream().flatMap(goal -> Stream.of(goal.value())).collect(Collectors.toList());
  }

  /**
   * @param context {@link ExtensionContext}
   * @return {@code true} if we have any {@link MavenOption @MavenOption} defined or {@code false} otherwise.
   */
  static boolean hasOptions(ExtensionContext context) {
    return options(context).size() > 0;
  }

  /**
   * Get the options from the annotation either on test method level
   * or on test class level.
   *
   * @param context {@link ExtensionContext}
   * @return The stream with the options if exist.
   */
  static List<String> options(ExtensionContext context) {
    List<MavenOption> mavenOptionsOnTestMethod = AnnotationSupport.findRepeatableAnnotations(context.getTestMethod(), MavenOption.class);
    List<String> options = mavenOptionsOnTestMethod.stream()
        .flatMap(option -> option.parameter().isEmpty() ? Stream.of(option.value()) : Stream.of(option.value(), option.parameter()))
        .collect(Collectors.toList());
    if (options.size() > 0) {
      return options;
    }

    List<MavenOption> mavenOptionsOnTestClass = AnnotationSupport.findRepeatableAnnotations(context.getTestClass(), MavenOption.class);
    return mavenOptionsOnTestClass.stream().flatMap(option -> option.parameter().isEmpty() ? Stream.of(option.value()) : Stream.of(option.value(), option.parameter()))
        .collect(Collectors.toList());
  }

  /**
   * @param context {@link ExtensionContext}
   * @return {@code true} if we have any {@link SystemProperty @SystemProperty} defined or {@code false} otherwise.
   */
  static boolean hasSystemProperties(ExtensionContext context) {
    return !systemProperties(context).isEmpty();
  }

  static List<SystemProperty> systemProperties(ExtensionContext context) {
    List<SystemProperty> systemPropertiesOnTestMethod = AnnotationSupport.findRepeatableAnnotations(context.getTestMethod(), SystemProperty.class);
    if (systemPropertiesOnTestMethod.stream().flatMap(s -> s.value().isEmpty() ? Stream.of(s.value()) : Stream.of(s.value(), s.value())).count() > 0) {
      return systemPropertiesOnTestMethod;
    }

    return AnnotationSupport.findRepeatableAnnotations(context.getTestClass(), SystemProperty.class);
  }

  private static Optional<Class<?>> findAnnotation(ExtensionContext context,
                                                   Class<? extends Annotation> annotationClass) {
    Optional<ExtensionContext> current = Optional.of(context);
    while (current.isPresent()) {
      Optional<Class<?>> testClassNumber1 = current.get().getTestClass();
      if (testClassNumber1.isPresent()) {
        Class<?> testClass = testClassNumber1.get();
        if (testClass.isAnnotationPresent(annotationClass)) {
          return Optional.of(testClass);
        }
      }
      current = current.get().getParent();
    }
    return Optional.empty();
  }

  private static <T extends Annotation> Optional<T> findAnnotationInHierarchy(ExtensionContext context,
                                                   Class<T> annotationClass) {
    Optional<ExtensionContext> current = Optional.of(context);
    while (current.isPresent()) {
      ExtensionContext currentContext = current.get();
      Optional<T> annotation = AnnotationSupport.findAnnotation(currentContext.getElement(), annotationClass);
      if (annotation.isPresent()) {
        return annotation;
      }
      current = currentContext.getParent();
    }
    return Optional.empty();
  }

  static Optional<Class<?>> findMavenRepositoryAnnotation(ExtensionContext context) {
    return findAnnotation(context, MavenRepository.class);
  }

  static Optional<Class<?>> findMavenProjectAnnotation(ExtensionContext context) {
    return findAnnotation(context, MavenProject.class);
  }

  static Optional<Class<?>> findMavenPredefinedRepositoryAnnotation(ExtensionContext context) {
    return findAnnotation(context, MavenPredefinedRepository.class);
  }

  static Optional<MavenSourceProject> findMavenSourceProjectAnnotation(ExtensionContext context) {
    return findAnnotationInHierarchy(context, MavenSourceProject.class);
  }

}
