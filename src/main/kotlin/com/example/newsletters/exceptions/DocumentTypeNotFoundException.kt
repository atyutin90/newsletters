package com.example.newsletters.exceptions

import com.example.newsletters.entity.enum.DocumentTemplateType

class DocumentTypeNotFoundException(type: DocumentTemplateType?) : RuntimeException(type?.name)
