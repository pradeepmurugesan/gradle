/*
 * Copyright 2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.api.tasks

import org.gradle.integtests.fixtures.AbstractIntegrationSpec

class TaskDeprecationsIntegrationTest extends AbstractIntegrationSpec {
    def "nags when project is accessed by task action"() {
        buildFile """
            tasks.register("ok") { t ->
                t.project.configurations.findByName("implementation")
            }

            tasks.register("broken") {
                doLast { t ->
                    t.project.configurations.findByName("implementation")
                }
            }
        """

        when:
        run("ok")

        then:
        noExceptionThrown()

        when:
        executer.expectDocumentedDeprecationWarning("Invocation of task.project at execution time has been deprecated. This will fail with an error in Gradle 8.0. Consult the upgrading guide for further information: https://docs.gradle.org/current/userguide/upgrading_version_7.html#task_project_at_execution_time")
        run("broken")

        then:
        noExceptionThrown()
    }
}
