package com.hellozjf.test.mark12306.controller;

import com.hellozjf.test.mark12306.constant.ResultEnum;
import com.hellozjf.test.mark12306.dataobject.VerificationCode;
import com.hellozjf.test.mark12306.repository.VerificationCodeRepository;
import com.hellozjf.test.mark12306.util.ResultUtils;
import com.hellozjf.test.mark12306.vo.QuestionInfoVO;
import com.hellozjf.test.mark12306.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author hellozjf
 */
@Controller
public class Mark12306Controller {

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    /**
     * 获取下一个没有回答的问题
     * @return
     */
    private QuestionInfoVO getNextUncheckQuestion() {

        VerificationCode verificationCode = verificationCodeRepository.findTopByChooseNullOrderByFolderNameAsc();
        if (verificationCode == null) {
            return null;
        }
        String folderName = verificationCode.getFolderName();

        QuestionInfoVO questionInfoVO = new QuestionInfoVO();
        questionInfoVO.setFolderName(folderName);
        String prefix = "https://aliyun.hellozjf.com:7004/Pictures/12306/";
        questionInfoVO.setQuestionUrl(prefix + folderName + "/question.jpg");
        questionInfoVO.setPic00Url(prefix + folderName + "/pic00.jpg");
        questionInfoVO.setPic01Url(prefix + folderName + "/pic01.jpg");
        questionInfoVO.setPic02Url(prefix + folderName + "/pic02.jpg");
        questionInfoVO.setPic03Url(prefix + folderName + "/pic03.jpg");
        questionInfoVO.setPic10Url(prefix + folderName + "/pic10.jpg");
        questionInfoVO.setPic11Url(prefix + folderName + "/pic11.jpg");
        questionInfoVO.setPic12Url(prefix + folderName + "/pic12.jpg");
        questionInfoVO.setPic13Url(prefix + folderName + "/pic13.jpg");
        return questionInfoVO;
    }

    @GetMapping("/getUncheckQuestion")
    @ResponseBody
    public ResultVO getUncheckQuestion() {

        QuestionInfoVO questionInfoVO = getNextUncheckQuestion();
        if (questionInfoVO == null) {
            return ResultUtils.error(ResultEnum.CAN_NOT_GET_UNANSWERED_QUESTION.getCode(),
                    ResultEnum.CAN_NOT_GET_UNANSWERED_QUESTION.getMessage());
        }
        return ResultUtils.success(questionInfoVO);
    }

    @PostMapping("/answerQuestion")
    @ResponseBody
    public ResultVO answerQuestion(QuestionInfoVO questionInfoVO) {

        // 出错检查
        if (StringUtils.isEmpty(questionInfoVO.getQuestion())) {
            return ResultUtils.error(ResultEnum.QUESTION_IS_EMPTY);
        } else if (StringUtils.isEmpty(questionInfoVO.getChoose())) {
            return ResultUtils.error(ResultEnum.ANSWER_IS_EMPTY);
        } else if (StringUtils.isEmpty(questionInfoVO.getFolderName())) {
            return ResultUtils.error(ResultEnum.FOLDER_NAME_IS_EMPTY);
        }

        // 更新数据库
        VerificationCode verificationCode = verificationCodeRepository.findByFolderName(questionInfoVO.getFolderName());
        verificationCode.setChoose(questionInfoVO.getChoose());
        verificationCode.setQuestion(questionInfoVO.getQuestion());
        verificationCodeRepository.save(verificationCode);

        QuestionInfoVO nextQuestionInfoVO = getNextUncheckQuestion();
        return ResultUtils.success(nextQuestionInfoVO);
    }
}
