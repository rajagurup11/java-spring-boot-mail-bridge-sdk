import React from 'react';
import { act } from '@testing-library/react';
import ConfigEditor from './ConfigEditor'; // Replace with your actual component file

        jest.useFakeTimers();

describe('setAlertData()', () => {
it('should handle successful save path', () => {
        const mockSaveConfig = jest.fn((_, __, ___, callback) => {
callback(); // simulate async callback
    });

            const mockDisplayAlertMsg = jest.fn();
    const mockGetData = jest.fn();
    const mockCloseModel = jest.fn();

    const props = {
saveconfig: mockSaveConfig,
data: { status: 200 },
        };

        const component = new ConfigEditor(props);
component.state = {
editMode: 'false',
currentUser: 'testUser',
data: {},
        };

// Mock methods
component.displayAlertMsg = mockDisplayAlertMsg;
component.getData = mockGetData;
component.closeAddAlertModel = mockCloseModel;
component.setState = jest.fn();

    const dummyData = {}; // input to setAlertData

        component.setAlertData(dummyData);

// Fast-forward the timeout
act(() => {
        jest.runAllTimers();
    });

expect(dummyData.createdBy).toBe('testUser');
expect(mockSaveConfig).toHaveBeenCalledWith(null, 'testUser', dummyData, expect.any(Function));
expect(component.setState).toHaveBeenCalledWith({ alertMsg: 'Saved data' });
expect(mockDisplayAlertMsg).toHaveBeenCalledWith('success');
expect(mockGetData).toHaveBeenCalled();
expect(mockCloseModel).toHaveBeenCalled();
  });

it('should handle error response', () => {
        const mockSaveConfig = jest.fn((_, __, ___, callback) => {
callback(); // simulate async callback
    });

            const mockDisplayAlertMsg = jest.fn();
    const props = {
saveconfig: mockSaveConfig,
data: { status: 500, errors: 'Something went wrong' },
        };

        const component = new ConfigEditor(props);
component.state = {
editMode: 'false',
currentUser: 'testUser',
data: {},
        };

component.displayAlertMsg = mockDisplayAlertMsg;
component.setState = jest.fn();
component.closeAddAlertModel = jest.fn();

    const dummyData = {};

        component.setAlertData(dummyData);

act(() => {
        jest.runAllTimers();
    });

expect(component.setState).toHaveBeenCalledWith({ alertMsg: 'Incorrect data' });
expect(mockDisplayAlertMsg).toHaveBeenCalledWith('error');
expect(component.closeAddAlertModel).toHaveBeenCalled();
  });

it('should skip when editMode is not false', () => {
        const mockSaveConfig = jest.fn();

    const component = new ConfigEditor({ saveconfig: mockSaveConfig });
component.state = {
editMode: 'true',
currentUser: 'testUser',
data: {},
        };

        component.setAlertData({}); // should early exit

expect(mockSaveConfig).not.toHaveBeenCalled();
  });
          });
