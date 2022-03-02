package com.wuhaibin.tools.mysqltool;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;

@Slf4j
public class MysqlConnect {
    private String url;
    private String driverName;
    private String username;
    private String passwd;
    private Connection con;
    private PreparedStatement pstm;
    public static ObjectMapper objectMapper = new ObjectMapper();
    public MysqlConnect(String url, String driverName, String username, String passwd){
        this.url = url;
        this.driverName = driverName;
        this.username = username;
        this.passwd = passwd;
        init();
    }
    private void init(){
        try {
            Class.forName(driverName);
            con = DriverManager.getConnection(url,username,passwd);
        }catch (Exception e){
            log.error("error");
        }
    }
    public void excute(String sql){
        try {
            pstm = con.prepareStatement(sql);
            pstm.executeBatch();
        }catch (Exception e){

        }
    }
    public String excuteQuery(String sql){
        String result;
        try {
            pstm = con.prepareStatement(sql);
            ResultSet resultSet = pstm.executeQuery();
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount();
            ObjectNode objectNode = objectMapper.createObjectNode();
            while (resultSet.next()){
                for(int i = 1; i < columnCount; i++){
                    String ColumnName = resultSetMetaData.getColumnName(i);
                    objectNode.put(ColumnName,resultSet.getString(ColumnName));
                }
            }
            result = objectMapper.writeValueAsString(objectNode);
            return result;
        }catch (Exception e){
            return "";
        }

    }

    public static void main(String[] args) {
        String json = "{\"name\":\"wuhaibin\",\"age\":\"25\",\"work\":\"tester\"}";
        try {
            JsonNode jsonNode = objectMapper.readTree(json);
            System.out.println(jsonNode.get("name").asText());
            System.out.println(jsonNode.get("age").asText());
            System.out.println(jsonNode.get("work").asText());
        }catch (Exception e){}
    }

}
