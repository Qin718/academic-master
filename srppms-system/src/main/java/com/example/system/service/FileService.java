package com.example.system.service;


import com.example.common.response.R;
import com.example.system.domain.entity.File;

import java.util.List;

public interface FileService {

    List<File> getFileList(Integer id);

    R<File> downloadFile(Integer id);
}

