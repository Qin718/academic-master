package com.example.web.controller;

import com.example.common.response.R;
import com.example.system.domain.bo.ProjectBo;
import com.example.system.domain.model.ProjectKind;
import com.example.system.domain.model.ProjectKindInfo;
import com.example.system.service.ProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(value = "项目内容管理", tags = {"项目内容管理"})
@RequestMapping(value = "/project", method = {RequestMethod.POST, RequestMethod.GET})
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @ResponseBody
    @RequestMapping("/score")
    @ApiOperation("用户对某一项目进行评分")
    public R<String> projectScore(@RequestBody ProjectBo bo) {
        return projectService.projectScore(bo);
    }

    @ResponseBody
    @ApiOperation("项目统计-获取今年科研项目的种类")
    @RequestMapping("/getList/kind/thisYear")
    public R<List<ProjectKind>> getKindThisYear() {
        return R.ok(projectService.getProjectKindThisYear());
    }

    @ResponseBody
    @ApiOperation("项目统计-获取去年科研项目的种类")
    @RequestMapping("/getList/kind/lastYear")
    public R<List<ProjectKind>> getKindLastYear() {
        return R.ok(projectService.getProjectKindLastYear());
    }

    @ResponseBody
    @ApiOperation("项目统计-今年")
    @RequestMapping("/getList/thisYear")
    public R<ProjectKindInfo> projectKindInfoListThisYear() {
        return R.ok(projectService.getProjectKindInfoThisYear());
    }

    @ResponseBody
    @ApiOperation("项目统计-去年")
    @RequestMapping("/getList/lastYear")
    public R<ProjectKindInfo> projectKindInfoListLastYear() {
        return R.ok(projectService.getProjectKindInfoLastYear());
    }

    @ResponseBody
    @RequestMapping("/number")
    @ApiOperation("今年发表的项目数量列表")
    public R<List<Integer>> getNumber() {
        return R.ok(projectService.getNumber());
    }
}
