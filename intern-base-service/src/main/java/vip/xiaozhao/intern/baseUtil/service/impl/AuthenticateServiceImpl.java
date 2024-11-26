package vip.xiaozhao.intern.baseUtil.service.impl;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vip.xiaozhao.intern.baseUtil.intf.entity.Authenticate;
import vip.xiaozhao.intern.baseUtil.intf.mapper.AuthenticateMapper;
import vip.xiaozhao.intern.baseUtil.intf.service.AuthenticateService;
import vip.xiaozhao.intern.baseUtil.intf.utils.StringUtil;
import vip.xiaozhao.intern.baseUtil.intf.utils.uploadUtils.Base64ImageUtils;
import vip.xiaozhao.intern.baseUtil.intf.utils.uploadUtils.UploadUtils;

@Service
public class AuthenticateServiceImpl implements AuthenticateService {

    @Resource
    private AuthenticateMapper authenticateMapper;

    @Resource
    private UploadUtils uploadUtils;

    @Override
    public Integer insertAuthenticate(String code, Authenticate authenticate) {
        String type = authenticate.getType();
        String studyCard = authenticate.getStudyCard();
        // 邮箱或图片校验
        if (type.equals("1")) {
            // 验证邮箱
            // 对 code 做一些处理，处理完后再插入数据库
            if (StringUtil.isEmpty(code) || StringUtil.isEmpty(authenticate.getEmail())) {
                throw new RuntimeException("邮箱和验证码不能为空！");
            }
            // TODO 后续业务
        } else {
            // 验证证件照
            if (StringUtil.isEmpty(studyCard)) {
                throw new RuntimeException("证件照不能为空！");
            }
            // 解码图片
            MultipartFile file = Base64ImageUtils.decodeToMultipartFile(studyCard);
            // 上传
            String url = uploadUtils.upload(file);
            if (url.equals("上传失败！")) {
                throw new RuntimeException("上传失败！");
            }
            // 设置图片路径
            authenticate.setStudyCard(url);
        }
        // TODO 查询学校名称并插入
        authenticateMapper.insertAuthenticate(authenticate);
        return authenticate.getId();
    }
}
