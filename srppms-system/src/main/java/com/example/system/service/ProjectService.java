package com.example.system.service;


import com.example.common.response.R;
import com.example.system.domain.bo.ProjectBo;
import com.example.system.domain.model.ProjectKind;
import com.example.system.domain.model.ProjectKindInfo;

import java.util.List;

public interface ProjectService {
    List<ProjectKind> getProjectKindThisYear();

    List<ProjectKind> getProjectKindLastYear();

    ProjectKindInfo getProjectKindInfoThisYear();

    ProjectKindInfo getProjectKindInfoLastYear();

    R<String> projectScore(ProjectBo bo);

    List<Integer> getNumber();
}
