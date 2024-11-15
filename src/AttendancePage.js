import React, { useState } from 'react';
import styled from 'styled-components';

// Estilos reutilizáveis
const Container = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f5f5f5;
`;

const AttendanceBox = styled.div`
  width: 100%;
  max-width: 600px;
  padding: 2rem;
  background: #ffffff;
  border-radius: 10px;
  box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
`;

const Title = styled.h2`
  color: #4caf50;
  text-align: center;
  margin-bottom: 1rem;
`;

const Subtitle = styled.h3`
  color: #333;
  margin-top: 1rem;
  font-size: 1.1rem;
`;

const Info = styled.p`
  font-size: 1rem;
  color: #666;
`;

const StudentRow = styled.div`
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 1rem;
`;

const Slider = styled.input.attrs({ type: 'checkbox' })`
  appearance: none;
  width: 40px;
  height: 20px;
  background-color: #ccc;
  border-radius: 15px;
  position: relative;
  outline: none;
  cursor: pointer;
  &:checked {
    background-color: #4caf50;
  }
  &:before {
    content: '';
    position: absolute;
    width: 18px;
    height: 18px;
    border-radius: 50%;
    background-color: white;
    top: 1px;
    left: 1px;
    transition: transform 0.2s;
  }
  &:checked:before {
    transform: translateX(20px);
  }
`;

const Input = styled.input`
  width: 100%;
  padding: 0.8rem;
  margin-bottom: 1rem;
  border: 1px solid #ccc;
  border-radius: 8px;
  font-size: 1rem;
  box-sizing: border-box;
`;

function AttendancePage() {
  const [students] = useState([
    'Aluno 1',
    'Aluno 2',
    'Aluno 3',
    'Aluno 4',
  ]);

  return (
    <Container>
      <AttendanceBox>
        <Title>Controle de Presença</Title>
        <Info>Professor: João Silva</Info>
        <Info>Turma: Turma de Matemática</Info>

        <Subtitle>Lista de Alunos</Subtitle>
        {students.map((student, index) => (
          <StudentRow key={index}>
            <span>{student}</span>
            <Slider />
          </StudentRow>
        ))}

        <Subtitle>Outras Informações</Subtitle>
        <Input type="number" placeholder="Valor da Oferta" />
        <Input type="number" placeholder="Número de Bíblias" />
        <Input type="number" placeholder="Número de Revistas" />
      </AttendanceBox>
    </Container>
  );
}

export default AttendancePage;
