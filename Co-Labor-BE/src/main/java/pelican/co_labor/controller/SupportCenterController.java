package pelican.co_labor.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pelican.co_labor.domain.support_center.SupportCenter;
import pelican.co_labor.service.SupportCenterService;

import java.util.List;

@Tag(name = "SupportCenter", description = "외국인 근로자 지원 센터 관련 API")
@RestController
@RequestMapping("/api/support-centers")
public class SupportCenterController {

    private final SupportCenterService supportCenterService;

    @Autowired
    public SupportCenterController(SupportCenterService supportCenterService) {
        this.supportCenterService = supportCenterService;
    }

    @Operation(summary = "외국인 근로자 지원 센터 데이터 Fetch", description = "API를 통해 외국인 근로자 지원 센터 데이터를 가져옵니다.")
    @GetMapping("/fetch")
    public void fetchData() {
        supportCenterService.fetchDataFromApi();
    }

    @Operation(summary = "모든 지원 센터 조회", description = "등록된 모든 외국인 근로자 지원 센터를 조회합니다.")
    @GetMapping("/all")
    public List<SupportCenter> getAllSupportCenters() {
        return supportCenterService.getAllSupportCenters();
    }
}
