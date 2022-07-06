package cn.cqut.lgqs.db.service;

import cn.cqut.lgqs.db.dao.LgqsSystemMapper;
import cn.cqut.lgqs.db.domain.LgqsSystem;
import cn.cqut.lgqs.db.domain.LgqsSystemExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LgqsSystemConfigService {
    @Resource
    private LgqsSystemMapper systemMapper;

    public Map<String, String> queryAll() {
        LgqsSystemExample example = new LgqsSystemExample();
        example.or().andDeletedEqualTo(false);

        List<LgqsSystem> systemList = systemMapper.selectByExample(example);
        Map<String, String> systemConfigs = new HashMap<>();
        for (LgqsSystem item : systemList) {
            systemConfigs.put(item.getKeyName(), item.getKeyValue());
        }

        return systemConfigs;
    }

    public Map<String, String> listMail() {
        LgqsSystemExample example = new LgqsSystemExample();
        example.or().andKeyNameLike("lgqs_mall_%").andDeletedEqualTo(false);
        List<LgqsSystem> systemList = systemMapper.selectByExample(example);
        Map<String, String> data = new HashMap<>();
        for(LgqsSystem system : systemList){
            data.put(system.getKeyName(), system.getKeyValue());
        }
        return data;
    }

    public Map<String, String> listWx() {
        LgqsSystemExample example = new LgqsSystemExample();
        example.or().andKeyNameLike("lgqs_wx_%").andDeletedEqualTo(false);
        List<LgqsSystem> systemList = systemMapper.selectByExample(example);
        Map<String, String> data = new HashMap<>();
        for(LgqsSystem system : systemList){
            data.put(system.getKeyName(), system.getKeyValue());
        }
        return data;
    }

    public Map<String, String> listOrder() {
        LgqsSystemExample example = new LgqsSystemExample();
        example.or().andKeyNameLike("lgqs_order_%").andDeletedEqualTo(false);
        List<LgqsSystem> systemList = systemMapper.selectByExample(example);
        Map<String, String> data = new HashMap<>();
        for(LgqsSystem system : systemList){
            data.put(system.getKeyName(), system.getKeyValue());
        }
        return data;
    }

    public Map<String, String> listExpress() {
        LgqsSystemExample example = new LgqsSystemExample();
        example.or().andKeyNameLike("lgqs_express_%").andDeletedEqualTo(false);
        List<LgqsSystem> systemList = systemMapper.selectByExample(example);
        Map<String, String> data = new HashMap<>();
        for(LgqsSystem system : systemList){
            data.put(system.getKeyName(), system.getKeyValue());
        }
        return data;
    }

    public void updateConfig(Map<String, String> data) {
        for (Map.Entry<String, String> entry : data.entrySet()) {
            LgqsSystemExample example = new LgqsSystemExample();
            example.or().andKeyNameEqualTo(entry.getKey()).andDeletedEqualTo(false);

            LgqsSystem system = new LgqsSystem();
            system.setKeyName(entry.getKey());
            system.setKeyValue(entry.getValue());
            system.setUpdateTime(LocalDateTime.now());
            systemMapper.updateByExampleSelective(system, example);
        }

    }

    public void addConfig(String key, String value) {
        LgqsSystem system = new LgqsSystem();
        system.setKeyName(key);
        system.setKeyValue(value);
        system.setAddTime(LocalDateTime.now());
        system.setUpdateTime(LocalDateTime.now());
        systemMapper.insertSelective(system);
    }
}
