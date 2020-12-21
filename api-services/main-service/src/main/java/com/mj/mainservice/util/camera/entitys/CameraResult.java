package com.mj.mainservice.util.camera.entitys;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

/**
 * Created by MrJan on 2020/12/14
 */

@Data
public class CameraResult {
  @JSONField(name = "RspPageInfo")
  private  RspPageInfo RspPageInfo;

  @JSONField(name = "InfoList")
  private List<ResInfo>  InfoList;
}
