package com.wfms.service;

import com.wfms.Dto.NoteDTO;
import com.wfms.Dto.ObjectPaging;
import com.wfms.entity.Note;

public interface NoteService {
    Note createNote(Note note);
    Note updateNote(Note note);
    NoteDTO getDetailNote(Long noteId);
    ObjectPaging searchNote(ObjectPaging objectPaging);

}
