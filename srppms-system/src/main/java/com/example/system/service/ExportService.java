package com.example.system.service;


import com.example.common.response.R;

import java.util.List;
import java.util.Map;

public interface ExportService {
    R<Map<String, Object>> exportEmployees(List<Integer> ids);

}
