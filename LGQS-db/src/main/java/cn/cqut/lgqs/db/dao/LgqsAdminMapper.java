package cn.cqut.lgqs.db.dao;

import cn.cqut.lgqs.db.domain.LgqsAdmin;
import cn.cqut.lgqs.db.domain.LgqsAdminExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LgqsAdminMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_admin
     *
     * @mbg.generated
     */
    long countByExample(LgqsAdminExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_admin
     *
     * @mbg.generated
     */
    int deleteByExample(LgqsAdminExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_admin
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_admin
     *
     * @mbg.generated
     */
    int insert(LgqsAdmin record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_admin
     *
     * @mbg.generated
     */
    int insertSelective(LgqsAdmin record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_admin
     *
     * @mbg.generated
     */
    LgqsAdmin selectOneByExample(LgqsAdminExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_admin
     *
     * @mbg.generated
     */
    LgqsAdmin selectOneByExampleSelective(@Param("example") LgqsAdminExample example, @Param("selective") LgqsAdmin.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_admin
     *
     * @mbg.generated
     */
    List<LgqsAdmin> selectByExampleSelective(@Param("example") LgqsAdminExample example, @Param("selective") LgqsAdmin.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_admin
     *
     * @mbg.generated
     */
    List<LgqsAdmin> selectByExample(LgqsAdminExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_admin
     *
     * @mbg.generated
     */
    LgqsAdmin selectByPrimaryKeySelective(@Param("id") Integer id, @Param("selective") LgqsAdmin.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_admin
     *
     * @mbg.generated
     */
    LgqsAdmin selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_admin
     *
     * @mbg.generated
     */
    LgqsAdmin selectByPrimaryKeyWithLogicalDelete(@Param("id") Integer id, @Param("andLogicalDeleted") boolean andLogicalDeleted);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_admin
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") LgqsAdmin record, @Param("example") LgqsAdminExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_admin
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") LgqsAdmin record, @Param("example") LgqsAdminExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_admin
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(LgqsAdmin record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_admin
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(LgqsAdmin record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_admin
     *
     * @mbg.generated
     */
    int logicalDeleteByExample(@Param("example") LgqsAdminExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lgqs_admin
     *
     * @mbg.generated
     */
    int logicalDeleteByPrimaryKey(Integer id);
}