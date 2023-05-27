package com.wfms.service.impl;

import com.wfms.Dto.NoteDTO;
import com.wfms.Dto.ObjectPaging;
import com.wfms.Dto.ProjectDTO;
import com.wfms.Dto.TaskDTO;
import com.wfms.config.Const;
import com.wfms.entity.Note;
import com.wfms.entity.Task;
import com.wfms.repository.NoteRepository;
import com.wfms.service.NoteService;
import com.wfms.service.ProjectService;
import com.wfms.utils.DataUtils;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;


import java.util.ArrayList;
 import java.time.LocalDateTime; 
import java.util.List;
import java.util.Objects;
@Service
public class NoteServiceImpl implements NoteService {
    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private ProjectService projectService;
    @Override
    public Note createNote(Note note) {
        Assert.notNull(note.getContent(), Const.responseError.content_null);
        Assert.notNull(note.getProjectId(), Const.responseError.projectId_null);
        ProjectDTO p =projectService.getDetailProject(note.getProjectId());
        Assert.notNull(p,Const.responseError.project_notFound+note.getProjectId());
        note.setCreateDate(LocalDateTime.now());
        note.setStatus(1);
        note.setNoteId(null);
        note.setUpdateDate(null);
        return noteRepository.save(note);
    }

    @Override
    public Note updateNote(Note note) {
        Assert.notNull(note.getContent(),Const.responseError.content_null);
        Assert.notNull(note.getProjectId(),Const.responseError.projectId_null);
        Assert.notNull(note.getNoteId(),"NoteId must not be null");
        ProjectDTO p =projectService.getDetailProject(note.getProjectId());
        Assert.notNull(p,Const.responseError.project_notFound+note.getProjectId());
        Note note1=noteRepository.findById(note.getNoteId()).get();
        Assert.notNull(note1,"Not found note with ID "+note.getNoteId());
        note1.setUpdateDate(LocalDateTime.now());
        note1.setProjectId(note.getProjectId());
        note1.setStatus(note.getStatus());
        note1.setContent(note.getContent());
        note1.setTitle(note.getTitle());
        return noteRepository.save(note1);
    }

    @Override
    public NoteDTO getDetailNote(Long noteId) {
        Assert.notNull(noteId,"NoteId must not be null");
        Note note1=noteRepository.findById(noteId).get();
        Assert.notNull(note1,"Not found note with ID "+noteId);
        NoteDTO noteDTO = new NoteDTO();
        BeanUtils.copyProperties(note1,noteDTO);
        ProjectDTO p =projectService.getDetailProject(note1.getProjectId());
        noteDTO.setProjectDTO(p);
        return noteDTO;
    }

    @Override
    public ObjectPaging searchNote(ObjectPaging objectPaging) {
        Pageable pageable = PageRequest.of(objectPaging.getPage() - 1, objectPaging.getLimit(), Sort.by("noteId").descending());
        Page<Note> list = noteRepository.searchNotePaging(objectPaging.getProjectId(),Objects.nonNull(objectPaging.getKeyword()) ? objectPaging.getKeyword().toLowerCase() : null, pageable);
        List<NoteDTO> noteDTOList =new ArrayList<>();
        if(DataUtils.listNotNullOrEmpty(list.getContent())){
        list.getContent().forEach(o->{
            NoteDTO noteDTO = new NoteDTO();
            BeanUtils.copyProperties(o,noteDTO);
            ProjectDTO p =projectService.getDetailProject(o.getProjectId());
            noteDTO.setProjectDTO(p);
            noteDTOList.add(noteDTO);
        });
        }
        return ObjectPaging.builder().total((int) list.getTotalElements())
                .page(objectPaging.getPage())
                .limit(objectPaging.getLimit())
                .data(noteDTOList).build();
    }
}
