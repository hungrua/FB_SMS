package com.signupfacebook.Newlife_project_1.service.impl;

import com.signupfacebook.Newlife_project_1.model.dto.ProcessData;
import com.signupfacebook.Newlife_project_1.service.IProcessService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProcessService implements IProcessService {

    @Override
    public ProcessData sendProcess(ProcessData data, Integer totalPhoneNumber) {
        try {
            if(totalPhoneNumber == null || data.getIndex() == null) {
                return new ProcessData();
            }
            double percent = ((double)data.getIndex() / totalPhoneNumber) * 100;
            percent = Math.floor(percent * 10) / 10;
            String process = percent + "%";
            data.setProcess(process);
            return data;
        }
        catch (NullPointerException nul) {
            nul.printStackTrace();
            return new ProcessData();
        }
    }

    @Override
    public List<ProcessData> addProcess(List<ProcessData> listProcess, ProcessData data) {
        Boolean exisit = false;
        Integer index = null;
        for(int i = 0; i < listProcess.size(); i++) {
            ProcessData processData = listProcess.get(i);
            if(processData.getCurrent_phoneNumber().equals(data.getCurrent_phoneNumber())) {
                exisit = true;
                index = i;
                break;
            }
        }
        if(!exisit) {
            listProcess.add(data);
        }
        else {
            listProcess.set(index, data);
        }
        return listProcess;
    }
}
