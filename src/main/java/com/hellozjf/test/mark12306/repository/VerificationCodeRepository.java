package com.hellozjf.test.mark12306.repository;

import com.hellozjf.test.mark12306.dataobject.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author hellozjf
 */
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, String> {

    /**
     * 通过folderName找到对应的VerificationCode
     * @param folderName
     * @return
     */
    VerificationCode findByFolderName(String folderName);

    /**
     * 找到时间最早的未标注的问题
     * @return
     */
    VerificationCode findTopByChooseNullOrderByFolderNameAsc();

    /**
     * 找到下一个未标注的问题
     * @return
     */
    VerificationCode findTopByChooseNullAndFolderNameGreaterThanOrderByFolderNameAsc(String folderName);

    /**
     * 找到上一个未标注的问题
     * @param folderName
     * @return
     */
    VerificationCode findTopByChooseNullAndFolderNameLessThanOrderByFolderNameDesc(String folderName);

    /**
     * 找到比某个问题时间更大的问题
     * @return
     */
    VerificationCode findTopByFolderNameGreaterThanOrderByFolderNameAsc(String folderName);

    /**
     * 找到比某个问题时间更小的问题
     * @param folderName
     * @return
     */
    VerificationCode findTopByFolderNameLessThanOrderByFolderNameDesc(String folderName);
}
