package io.proleap.cobol.engine.ir;

import java.util.ArrayList;
import java.util.List;

import io.proleap.cobol.asg.metamodel.ProgramUnit;
import io.proleap.cobol.asg.metamodel.identification.IdentificationDivision;
import io.proleap.cobol.asg.metamodel.identification.ProgramIdParagraph;
import io.proleap.cobol.asg.metamodel.procedure.ProcedureDivision;
import io.proleap.cobol.asg.metamodel.procedure.Statement;
import io.proleap.cobol.asg.metamodel.procedure.StatementTypeEnum;
import io.proleap.cobol.asg.metamodel.procedure.display.DisplayStatement;
import io.proleap.cobol.asg.metamodel.procedure.display.Operand;
import io.proleap.cobol.asg.metamodel.procedure.stop.StopStatement;
import io.proleap.cobol.asg.metamodel.valuestmt.LiteralValueStmt;
import io.proleap.cobol.asg.metamodel.valuestmt.ValueStmt;
import io.proleap.cobol.engine.parse.ParseArtifact;

public class IrProgramBuilder {

    public IrProgram build(final ParseArtifact artifact) {
        final ProgramUnit programUnit = artifact.getProgram().getCompilationUnit().getProgramUnit();
        final String programName = resolveProgramName(programUnit, artifact.getCompilationUnitName());
        final List<IrStatement> statements = new ArrayList<IrStatement>();

        final ProcedureDivision procedureDivision = programUnit.getProcedureDivision();
        if (procedureDivision != null) {
            for (final Statement statement : procedureDivision.getStatements()) {
                if (statement.getStatementType() == StatementTypeEnum.DISPLAY && statement instanceof DisplayStatement) {
                    for (final Operand operand : ((DisplayStatement) statement).getOperands()) {
                        statements.add(new IrStatement(IrStatement.Type.DISPLAY, resolveDisplayText(operand.getOperandValueStmt()),
                                provenance(statement)));
                    }
                } else if (statement.getStatementType() == StatementTypeEnum.STOP && statement instanceof StopStatement) {
                    statements.add(new IrStatement(IrStatement.Type.STOP,
                            ((StopStatement) statement).getStopType() == null ? "STOP" : ((StopStatement) statement).getStopType().name(),
                            provenance(statement)));
                } else {
                    statements.add(new IrStatement(IrStatement.Type.UNSUPPORTED, statement.getStatementType().toString(), provenance(statement)));
                }
            }
        }

        return new IrProgram("v1", programName, statements, artifact.getDiagnostics());
    }

    private String provenance(final Statement statement) {
        if (statement.getCtx() == null || statement.getCtx().getStart() == null) {
            return "unknown";
        }
        return "line:" + statement.getCtx().getStart().getLine();
    }

    private String resolveDisplayText(final ValueStmt valueStmt) {
        if (valueStmt instanceof LiteralValueStmt) {
            final Object value = ((LiteralValueStmt) valueStmt).getLiteral().getValue();
            return normalizeLiteral(value);
        }
        return valueStmt == null ? "" : valueStmt.toString();
    }

    private String normalizeLiteral(final Object value) {
        if (value == null) {
            return "";
        }
        String text = String.valueOf(value);
        if (text.length() >= 2) {
            final char first = text.charAt(0);
            final char last = text.charAt(text.length() - 1);
            if ((first == '"' && last == '"') || (first == '\'' && last == '\'')) {
                text = text.substring(1, text.length() - 1);
            }
        }
        return text;
    }

    private String resolveProgramName(final ProgramUnit programUnit, final String fallbackName) {
        final IdentificationDivision identificationDivision = programUnit.getIdentificationDivision();
        if (identificationDivision != null) {
            final ProgramIdParagraph programIdParagraph = identificationDivision.getProgramIdParagraph();
            if (programIdParagraph != null && programIdParagraph.getName() != null) {
                final String parsedName = programIdParagraph.getName();
                if (fallbackName != null && !fallbackName.isEmpty() && parsedName.equals(parsedName.toUpperCase())) {
                    return fallbackName;
                }
                return parsedName;
            }
        }
        return fallbackName;
    }
}
