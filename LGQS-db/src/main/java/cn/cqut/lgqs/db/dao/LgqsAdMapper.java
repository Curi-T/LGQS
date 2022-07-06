package cn.cqut.lgqs.db.dao;

import cn.cqut.lgqs.db.domain.LgqsAd;
import cn.cqut.lgqs.db.domain.LgqsAdExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LgqsAdMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_ad
     *
     * @mbg.generated
     */
    long countByExample(LgqsAdExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_ad
     *
     * @mbg.generated
     */
    int deleteByExample(LgqsAdExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_ad
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_ad
     *
     * @mbg.generated
     */
    int insert(LgqsAd record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_ad
     *
     * @mbg.generated
     */
    int insertSelective(LgqsAd record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_ad
     *
     * @mbg.generated
     */
    LgqsAd selectOneByExample(LgqsAdExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_ad
     *
     * @mbg.generated
     */
    LgqsAd selectOneByExampleSelective(@Param("example") LgqsAdExample example, @Param("selective") LgqsAd.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_ad
     *
     * @mbg.generated
     */
    List<LgqsAd> selectByExampleSelective(@Param("example") LgqsAdExample example, @Param("selective") LgqsAd.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_ad
     *
     * @mbg.generated
     */
    List<LgqsAd> selectByExample(LgqsAdExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_ad
     *
     * @mbg.generated
     */
    LgqsAd selectByPrimaryKeySelective(@Param("id") Integer id, @Param("selective") LgqsAd.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_ad
     *
     * @mbg.generated
     */
    LgqsAd selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_ad
     *
     * @mbg.generated
     */
    LgqsAd selectByPrimaryKeyWithLogicalDelete(@Param("id") Integer id, @Param("andLogicalDeleted") boolean andLogicalDeleted);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_ad
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") LgqsAd record, @Param("example") LgqsAdExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_ad
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") LgqsAd record, @Param("example") LgqsAdExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_ad
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(LgqsAd record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_ad
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(LgqsAd record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_ad
     *
     * @mbg.generated
     */
    int logicalDeleteByExample(@Param("example") LgqsAdExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_ad
     *
     * @mbg.generated
     */
    int logicalDeleteByPrimaryKey(Integer id);
}