package cn.cqut.lgqs.wx.service;

import cn.cqut.lgqs.db.domain.LgqsRegion;
import cn.cqut.lgqs.db.service.LgqsRegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zhy
 * @date 2019-01-17 23:07
 **/
@Component
public class GetRegionService {

	@Autowired
	private LgqsRegionService regionService;

	private static List<LgqsRegion> lgqsRegions;

	protected List<LgqsRegion> getLgqsRegions() {
		if(lgqsRegions==null){
			createRegion();
		}
		return lgqsRegions;
	}

	private synchronized void createRegion(){
		if (lgqsRegions == null) {
			lgqsRegions = regionService.getAll();
		}
	}
}
