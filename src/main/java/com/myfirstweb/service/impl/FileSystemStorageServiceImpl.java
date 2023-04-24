package com.myfirstweb.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import com.myfirstweb.config.StorageProperties;
import com.myfirstweb.exception.StorageException;
import com.myfirstweb.exception.StorageFileNotFoundException;
import com.myfirstweb.service.StorageService;

@Service
public class FileSystemStorageServiceImpl implements StorageService{
	private final Path rootLocation; // đường dẫn gốc để lưu file
	@Override
	public String getStoredFilename(MultipartFile file, String id) { // tạo ra tên file để lưu trữ
		String ext = FilenameUtils.getExtension(file.getOriginalFilename());
		return "p" + id + "." + ext;
	}
	
	public FileSystemStorageServiceImpl(StorageProperties properties) { // hàm contructor để truyền thông tin cấu hình properties cho phần lưu trữ
		this.rootLocation = Paths.get(properties.getLocation());
	}
	
	@Override
	public void store(MultipartFile file, String storedFilename) { //lưu nội dung file từ thành phần multipart
		try {
			if(file.isEmpty()) {
				throw new StorageException("Failed to store empty file");
			}
			
			Path destinationFile = this.rootLocation.resolve(Paths.get(storedFilename)).normalize().toAbsolutePath();
			
			if(!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
				throw new StorageException("Cannot store file outside current directory");
			}
			try(InputStream inputStream = file.getInputStream()){
				Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (Exception e) {
			throw new StorageException("Failed to store file", e);
		}
	}
	
	@Override
	public Resource loadAsResource(String filename) { // nạp nội dung file dưới dạng réource
		try {
			Path file = load(filename);
			
			Resource resource = new UrlResource(file.toUri());
			if(resource.exists() || resource.isReadable()) {
				return resource;
			}
			
			throw new StorageFileNotFoundException("Could not read File: "+filename);
		} catch (Exception e) {
			throw new StorageFileNotFoundException("Could not read File: "+filename);
		}
	}
	
	@Override
	public Path load(String filename) {
		return rootLocation.resolve(filename);
	}
	
	@Override
	public void delete(String storedFilename) throws IOException {
		Path destinationFile = rootLocation.resolve(Paths.get(storedFilename)).normalize().toAbsolutePath();
		Files.delete(destinationFile);
	}
	
	@Override
	public void init() { // khởi tạo thư mục
		try {
			Files.createDirectories(rootLocation);
			System.out.println(rootLocation.toString());
		} catch (Exception e) {
			throw new StorageException("Could not initialize storage", e);
		}
	}
}
