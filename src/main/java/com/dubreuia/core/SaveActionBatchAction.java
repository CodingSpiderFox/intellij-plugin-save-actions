/*
 * Copyright 2000-2016 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dubreuia.core;

import com.intellij.analysis.AnalysisScope;
import com.intellij.analysis.BaseAnalysisAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicLong;

import static com.dubreuia.core.Component.COMPONENT_NAME;

/**
 * Runs the save actions on the given scope of files. The user is asked for the scope using a standard IDEA dialog.
 * <p>
 * Originally based on {@link com.intellij.codeInspection.inferNullity.InferNullityAnnotationsAction}
 *
 * @author markiewb
 */
public class SaveActionBatchAction extends BaseAnalysisAction {

    public static final Logger LOGGER = Logger.getInstance(SaveActionBatchAction.class);

    public SaveActionBatchAction() {
        super(COMPONENT_NAME, COMPONENT_NAME);
    }

    @Override
    protected void analyze(@NotNull Project project, @NotNull AnalysisScope scope) {
        LOGGER.debug("Running SaveActionBatchAction on " + project + " with scope " + scope);
        AtomicLong fileCount = new AtomicLong();
        scope.accept(new PsiElementVisitor() {
            @Override
            public void visitFile(PsiFile psiFile) {
                super.visitFile(psiFile);
                fileCount.incrementAndGet();
                for (SaveActionManager saveActionManager : SaveActionFactory.getSaveActionManagers()) {
                    saveActionManager.processPsiFile(project, psiFile, ExecutionMode.batch);
                }
            }
        });
        LOGGER.debug("Executed SaveActionBatchAction on " + fileCount.get() + " files ");
    }

}