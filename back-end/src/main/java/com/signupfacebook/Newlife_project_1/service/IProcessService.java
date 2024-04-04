package com.signupfacebook.Newlife_project_1.service;

import com.signupfacebook.Newlife_project_1.model.dto.ProcessData;

import java.util.List;

public interface IProcessService {
    ProcessData sendProcess(ProcessData data, Integer totalPhoneNumber);
    List<ProcessData> addProcess(List<ProcessData> listProcess, ProcessData data);
}
