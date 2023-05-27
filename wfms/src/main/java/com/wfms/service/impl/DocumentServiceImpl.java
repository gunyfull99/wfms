package com.wfms.service.impl;

import com.wfms.Dto.DocumentDto;
import com.wfms.Dto.ObjectPaging;
import com.wfms.Dto.ProjectDTO;
import com.wfms.config.Const;
import com.wfms.entity.Document;
import com.wfms.entity.Projects;
import com.wfms.entity.Users;
import com.wfms.repository.DocumentRepository;
import com.wfms.repository.ProjectRepository;
import com.wfms.service.DocumentService;
import com.wfms.service.ProjectService;
import com.wfms.service.UsersService;
import com.wfms.utils.DataUtils;
import com.wfms.utils.JwtUtility;
import com.wfms.utils.MinioUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


import java.util.ArrayList;
 import java.time.LocalDateTime; 
import java.util.List;
import java.util.Objects;

@Service
public class DocumentServiceImpl implements DocumentService {
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private UsersService usersService;
    @Autowired
    private JwtUtility jwtUtility;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private MinioUtils minioUtils;
    @Override
    public String createDocument(String token, Long projectId, String description, List<MultipartFile> files) {
        String jwtToken = token.substring(7);
        String username = jwtUtility.getUsernameFromToken(jwtToken);
        Users users =usersService.getByUsername(username);
        if(users==null) return null;
        Assert.notNull(projectId, Const.responseError.projectId_null);
        Projects projects = projectRepository.getById(projectId);
        Assert.notNull(projects, Const.responseError.project_notFound+projectId);
        if(Objects.nonNull(files) && !files.isEmpty()){
            for (int i = 0; i <files.size() ; i++) {
                String filename =null ;
                Document document=new Document();
                filename= StringUtils.cleanPath((Objects.requireNonNull(files.get(i).getOriginalFilename())));
                filename = filename.substring(0, filename.lastIndexOf("."))
                        .replace(".", "").replace(";","-") + "." + filename.substring(filename.lastIndexOf(".") + 1);
                filename=minioUtils.uploadFile(filename,files.get(i));
                document.setDocumentId(null);
                document.setCreateDate(LocalDateTime.now());
                document.setStatus(1);
                if(Objects.nonNull(description)){
                    document.setDescription(description);
                }
                document.setFileName(filename);
                document.setType( filename.substring(filename.lastIndexOf(".") + 1));
                document.setProjects(projects);
                document.setCreateUser(users.getId());
                document.setUrl(minioUtils.getFileUrl(filename));
                documentRepository.save(document);
            }
        return "Upload file successfull";
        }

        return "Upload file fail";
    }

    @Override
    public String deleteDocument(String token, Long documentId) {
//        String jwtToken = token.substring(7);
//        String username = jwtUtility.getUsernameFromToken(jwtToken);
//        Users users =usersService.getByUsername(username);
//        if(users==null) return null;
        Assert.notNull(documentId,"DocumentId must not be null");
        Document document=documentRepository.findById(documentId).get();
        Assert.notNull(document,"Not found document with ID "+documentId);
    //    Assert.notNull(document.getProjects().getProjectId(),"Mã dự án không được để trống");
      //  Projects projects = projectRepository.getById(document.getProjects().getProjectId());
      //  Assert.notNull(projects,"Không tìm thấy project với ID "+document.getProjects().getProjectId());
      //  Assert.isTrue(users.getId().equals(projects.getLead()),"Bạn không có quyền xóa document này");
        documentRepository.delete(document);
        return "Delete document successfull";
    }

    @Override
    public String updateDocument(Long documentId, String description) {
        Assert.notNull(documentId,"DocumentId must not be null");
        Document document=documentRepository.findById(documentId).get();
        Assert.notNull(document,"Not found document with ID "+documentId);
        document.setDescription(description);
        documentRepository.save(document);
        return "Update document successfull";
    }

    @Override
    public ObjectPaging getListFileInProject(ObjectPaging objectPaging) {
        Assert.notNull(objectPaging.getProjectId(),Const.responseError.projectId_null);
        Projects projects = projectRepository.getById(objectPaging.getProjectId());
        Assert.notNull(projects,Const.responseError.project_notFound+objectPaging.getProjectId());
        Pageable pageable = PageRequest.of(objectPaging.getPage() - 1, objectPaging.getLimit(), Sort.by("documentId").descending());
        Page<Document> list=documentRepository.getListFileInProject(objectPaging.getProjectId(),Objects.nonNull(objectPaging.getKeyword()) ? objectPaging.getKeyword().toLowerCase() : null,pageable);
        List<DocumentDto> dtoList=new ArrayList<>();
        if(DataUtils.listNotNullOrEmpty(list.getContent())){
            list.getContent().forEach(document -> {
                DocumentDto d=new DocumentDto();
                BeanUtils.copyProperties(document,d);
                d.setCreateUser(usersService.getUserById(document.getCreateUser()));
                ProjectDTO p =new ProjectDTO();
                BeanUtils.copyProperties(document.getProjects(),p);
                d.setProjects(p);
                dtoList.add(d);
            });
        }

        return ObjectPaging.builder().total((int) list.getTotalElements())
                .page(objectPaging.getPage())
                .limit(objectPaging.getLimit())
                .data(dtoList).build();
    }

}
