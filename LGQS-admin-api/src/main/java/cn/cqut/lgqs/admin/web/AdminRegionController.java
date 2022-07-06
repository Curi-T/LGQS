package cn.cqut.lgqs.admin.web;

import cn.cqut.lgqs.admin.vo.RegionVo;
import cn.cqut.lgqs.core.util.ResponseUtil;
import cn.cqut.lgqs.db.domain.LgqsRegion;
import cn.cqut.lgqs.db.service.LgqsRegionService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/region")
@Validated
public class AdminRegionController {
    private final Log logger = LogFactory.getLog(AdminRegionController.class);

    @Autowired
    private LgqsRegionService regionService;

    @GetMapping("/clist")
    public Object clist(@NotNull Integer id) {
        List<LgqsRegion> regionList = regionService.queryByPid(id);
        return ResponseUtil.okList(regionList);
    }

    @GetMapping("/list")
    public Object list() {
        List<RegionVo> regionVoList = new ArrayList<>();

        List<LgqsRegion> lgqsRegions = regionService.getAll();
        Map<Byte, List<LgqsRegion>> collect = lgqsRegions.stream().collect(Collectors.groupingBy(LgqsRegion::getType));
        byte provinceType = 1;
        List<LgqsRegion> provinceList = collect.get(provinceType);
        byte cityType = 2;
        List<LgqsRegion> city = collect.get(cityType);
        Map<Integer, List<LgqsRegion>> cityListMap = city.stream().collect(Collectors.groupingBy(LgqsRegion::getPid));
        byte areaType = 3;
        List<LgqsRegion> areas = collect.get(areaType);
        Map<Integer, List<LgqsRegion>> areaListMap = areas.stream().collect(Collectors.groupingBy(LgqsRegion::getPid));

        for (LgqsRegion province : provinceList) {
            RegionVo provinceVO = new RegionVo();
            provinceVO.setId(province.getId());
            provinceVO.setName(province.getName());
            provinceVO.setCode(province.getCode());
            provinceVO.setType(province.getType());

            List<LgqsRegion> cityList = cityListMap.get(province.getId());
            List<RegionVo> cityVOList = new ArrayList<>();
            for (LgqsRegion cityVo : cityList) {
                RegionVo cityVO = new RegionVo();
                cityVO.setId(cityVo.getId());
                cityVO.setName(cityVo.getName());
                cityVO.setCode(cityVo.getCode());
                cityVO.setType(cityVo.getType());

                List<LgqsRegion> areaList = areaListMap.get(cityVo.getId());
                List<RegionVo> areaVOList = new ArrayList<>();
                for (LgqsRegion area : areaList) {
                    RegionVo areaVO = new RegionVo();
                    areaVO.setId(area.getId());
                    areaVO.setName(area.getName());
                    areaVO.setCode(area.getCode());
                    areaVO.setType(area.getType());
                    areaVOList.add(areaVO);
                }

                cityVO.setChildren(areaVOList);
                cityVOList.add(cityVO);
            }
            provinceVO.setChildren(cityVOList);
            regionVoList.add(provinceVO);
        }

        return ResponseUtil.okList(regionVoList);
    }
}
