package com.ltt.overseasuser.model;

/**
 * Created by Administrator on 2018/7/4 0004.
 */

public class AttachmentFileBean {
     private String attachment_id;
      private String file_path;
     private String file_name;
      private String file_type;
      public void setAttachment_id(String attachment_id){this.attachment_id=attachment_id;}
      public String getAttachment_id(){return attachment_id;}
      public void setFile_path(String file_path){this.file_path=file_path;}
      public String getFile_path(){return file_path;}
      public void setFile_name(String file_name){this.file_name=file_name;}
      public String getFile_name(){return file_name;}
      public void setFile_type(String file_type){this.file_type=file_type;}
      public String getFile_type(){return file_type;}
}
