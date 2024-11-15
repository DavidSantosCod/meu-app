// src/components/AttendancePage.js

import React, { useState } from 'react';
import styled from 'styled-components';
import './LoginPage.css';
import { jsPDF } from 'jspdf';

const Container = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 2rem;
`;

const Title = styled.h1`
  color: #4caf50;
  margin-bottom: 1rem;
`;

const SubTitle = styled.h2`
  color: #333;
  margin-bottom: 1rem;
`;

const InputContainer = styled.div`
  display: flex;
  flex-direction: column;
  width: 100%;
  max-width: 400px;
  margin-bottom: 1rem;
`;

const Label = styled.label`
  font-weight: bold;
  margin-bottom: 0.5rem;
`;

const Input = styled.input`
  padding: 0.8rem;
  border: 1px solid #ccc;
  border-radius: 8px;
  font-size: 1rem;
  margin-bottom: 1rem;
`;

const StudentList = styled.div`
  width: 100%;
  max-width: 400px;
  margin-top: 1rem;
`;

const StudentItem = styled.div`
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 1rem;
  padding: 0.8rem;
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0px 2px 5px rgba(0, 0, 0, 0.1);
`;

const StudentName = styled.span`
  font-size: 1rem;
`;

const ToggleButton = styled.button`
  background-color: ${props => (props.present ? '#4caf50' : '#f44336')};
  color: white;
  border: none;
  border-radius: 8px;
  padding: 0.5rem 1rem;
  cursor: pointer;
  transition: background-color 0.3s;

  &:hover {
    background-color: ${props => (props.present ? '#45a049' : '#d32f2f')};
  }
`;

const ExportButton = styled.button`
  margin-top: 20px;
  padding: 10px 20px;
  background-color: #606062;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  
  &:hover {
    background-color: #fdd141;
  }
`;

function AttendancePage() {
  const [students, setStudents] = useState([
    { name: 'Daniel Nogueira', present: false },
    { name: 'Alan Sampaio', present: false },
    { name: 'Juliana Silva', present: false },
    { name: 'Carlos Almeida', present: false },
    { name: 'Beatriz Costa', present: false },
    { name: 'Felipe Souza', present: false },
    { name: 'Lucas Pereira', present: false },
    { name: 'Ana Oliveira', present: false },
    { name: 'Rafael Lima', present: false },
    { name: 'Mariana Fernandes', present: false },
    { name: 'Ricardo Martins', present: false },
    { name: 'Tatiane Rocha', present: false },
    { name: 'Gabriel Santos', present: false },
    { name: 'Larissa Ferreira', present: false },
    { name: 'Tiago Souza', present: false },
    { name: 'Jéssica Rodrigues', present: false },
    { name: 'Fernando Pinto', present: false },
    { name: 'Vanessa Mendes', present: false },
    { name: 'Paulo Silva', present: false },
    { name: 'Roberta Alves', present: false },
    { name: 'Marcelo Barbosa', present: false },
    { name: 'Eduarda Gomes', present: false },
    { name: 'João Viana', present: false },
  ]);

  const [offerValue, setOfferValue] = useState('');
  const [bibleCount, setBibleCount] = useState('');
  const [magazineCount, setMagazineCount] = useState('');

  const togglePresence = (index) => {
    const updatedStudents = [...students];
    updatedStudents[index].present = !updatedStudents[index].present;
    setStudents(updatedStudents);
  };

  const exportToPDF = () => {
    const doc = new jsPDF();

    doc.setFontSize(20);
    doc.text('RELATÓRIO DE PRESENÇAS', 20, 20);

    doc.setFontSize(14);
    doc.text(`Turma: JOVENS`, 20, 30);

    // Adicionar as informações da oferta, bíblias e revistas
    doc.text(`OFERTA DO DIA: R$ ${offerValue}`, 20, 40);
    doc.text(`NÚMEROS DE BÍBLIAS: ${bibleCount}`, 20, 50);
    doc.text(`NÚMEROS DE REVISTAS: ${magazineCount}`, 20, 60);

    let yPosition = 70;
    students.forEach((student, index) => {
      doc.text(`${index + 1}. ${student.name} - ${student.present ? 'Presente' : 'Falta'}`, 20, yPosition);
      yPosition += 10;
    });

    doc.save('relatorio_presenca_turma.pdf');
  };

  return (
    <Container>
      <h2>CONTROLE DE PRESENÇA</h2>
      <SubTitle>PROFESSOR: DAVID SANTOS </SubTitle>
      <SubTitle>TURMA: JOVENS</SubTitle>

      <InputContainer>
        <Label>OFERTA DO DIA:</Label>
        <Input
          type="number"
          placeholder="Valor da oferta"
          value={offerValue}
          onChange={(e) => setOfferValue(e.target.value)}
        />

        <Label>NÚMEROS DE BÍBLIAS:</Label>
        <Input
          type="number"
          placeholder="Quantidade de Bíblias"
          value={bibleCount}
          onChange={(e) => setBibleCount(e.target.value)}
        />

        <Label>NÚMERO DE REVISTAS:</Label>
        <Input
          type="number"
          placeholder="Quantidade de Revistas"
          value={magazineCount}
          onChange={(e) => setMagazineCount(e.target.value)}
        />
      </InputContainer>

      <StudentList>
        {students.map((student, index) => (
          <StudentItem key={index}>
            <StudentName>{student.name}</StudentName>
            <ToggleButton
              present={student.present}
              onClick={() => togglePresence(index)}
            >
              {student.present ? 'Presente' : 'Falta'}
            </ToggleButton>
          </StudentItem>
        ))}
      </StudentList>

      <ExportButton onClick={exportToPDF}>Exportar em PDF</ExportButton>
    </Container>
  );
}

export default AttendancePage;
