package com.HFS.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.HFS.Entity.Bank;

public interface BankRepository extends JpaRepository<Bank, String> {
}