package cn.cqut.lgqs.db.dao;

import cn.cqut.lgqs.db.domain.LgqsAftersale;
import cn.cqut.lgqs.db.domain.LgqsAftersaleExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LgqsAftersaleMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_aftersale
     *
     * @mbg.generated
     */
    long countByExample(LgqsAftersaleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_aftersale
     *
     * @mbg.generated
     */
    int deleteByExample(LgqsAftersaleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_aftersale
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_aftersale
     *
     * @mbg.generated
     */
    int insert(LgqsAftersale record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_aftersale
     *
     * @mbg.generated
     */
    int insertSelective(LgqsAftersale record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_aftersale
     *
     * @mbg.generated
     */
    LgqsAftersale selectOneByExample(LgqsAftersaleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_aftersale
     *
     * @mbg.generated
     */
    LgqsAftersale selectOneByExampleSelective(@Param("example") LgqsAftersaleExample example, @Param("selective") LgqsAftersale.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_aftersale
     *
     * @mbg.generated
     */
    List<LgqsAftersale> selectByExampleSelective(@Param("example") LgqsAftersaleExample example, @Param("selective") LgqsAftersale.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_aftersale
     *
     * @mbg.generated
     */
    List<LgqsAftersale> selectByExample(LgqsAftersaleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_aftersale
     *
     * @mbg.generated
     */
    LgqsAftersale selectByPrimaryKeySelective(@Param("id") Integer id, @Param("selective") LgqsAftersale.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_aftersale
     *
     * @mbg.generated
     */
    LgqsAftersale selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_aftersale
     *
     * @mbg.generated
     */
    LgqsAftersale selectByPrimaryKeyWithLogicalDelete(@Param("id") Integer id, @Param("andLogicalDeleted") boolean andLogicalDeleted);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_aftersale
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") LgqsAftersale record, @Param("example") LgqsAftersaleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_aftersale
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") LgqsAftersale record, @Param("example") LgqsAftersaleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_aftersale
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(LgqsAftersale record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_aftersale
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(LgqsAftersale record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_aftersale
     *
     * @mbg.generated
     */
    int logicalDeleteByExample(@Param("example") LgqsAftersaleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_aftersale
     *
     * @mbg.generated
     */
    int logicalDeleteByPrimaryKey(Integer id);
}