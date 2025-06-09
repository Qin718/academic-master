package com.example.system.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.system.domain.entity.Project;
import com.example.system.domain.model.ProjectKind;
import com.example.system.domain.model.ProjectKindInfo;
import com.example.system.domain.vo.ProjectVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (SysProject)表数据库访问层
 */
@Mapper
public interface ProjectMapper extends BaseMapper<Project> {
    List<ProjectKind> getProjectKindList(@Param("year") String year);

    int insertProject(Project project);

    ProjectKindInfo selectProjectByYear(@Param("year") String year);

    List<ProjectVo> getProjectListByYear(@Param("year") String year);
}

